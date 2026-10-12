package com.seb.eventpass.orders.services.order;

import com.seb.eventpass.orders.dto.requests.CreateOrderDTO;
import com.seb.eventpass.orders.dto.responses.OrderDTO;
import com.seb.eventpass.orders.dto.responses.TicketOrderedDTO;
import com.seb.eventpass.orders.dto.rest.TicketDTO;
import com.seb.eventpass.orders.models.Order;
import com.seb.eventpass.orders.models.OrderStatus;
import com.seb.eventpass.orders.models.TicketOrder;
import com.seb.eventpass.orders.repositories.OrderRepository;
import com.seb.eventpass.orders.repositories.TicketOrderRepository;
import com.seb.eventpass.orders.rest.EventClient;
import com.seb.eventpass.orders.rest.TicketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final EventClient eventWebClient;
    private final OrderRepository orderRepository;
    private final TicketOrderRepository ticketOrderRepository;
    private final TicketClient ticketClient;

    @Override
    @Transactional
    public Mono<OrderDTO> createOrder(CreateOrderDTO createOrderDTO, UUID userUuid) {
        return Flux.fromIterable(createOrderDTO.ticketsOrdered())
                .flatMap(ticketOrdered ->
                        eventWebClient.getTicketCategory(createOrderDTO.eventUuid(), ticketOrdered.ticketCategoryUuid())
                                .map(ticketCategoryDTO ->
                                        TicketOrder.builder()
                                                .ticketCategoryUuid(ticketCategoryDTO.ticketCategoryUuid())
                                                .ticketPrice(ticketCategoryDTO.price())
                                                .ticketCategoryName(ticketCategoryDTO.categoryName())
                                                .ticketQuantity(ticketOrdered.ticketQuantity())
                                                .build()))
                .collectList()
                .flatMap(ticketsOrdered -> {
                    Integer ticketsTotal = ticketsOrdered.stream().mapToInt(TicketOrder::getTicketQuantity).sum();
                    BigDecimal ticketsTotalPrice = ticketsOrdered.stream().map(ticket -> {
                                BigDecimal ticketQuantity = new BigDecimal(ticket.getTicketQuantity());
                                return ticket.getTicketPrice().multiply(ticketQuantity);
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);


                    Order order = Order.builder()
                            .orderStatus(OrderStatus.ACCEPTED)
                            .ticketsOrdered(ticketsTotal)
                            .totalAmount(ticketsTotalPrice)
                            .eventUuid(createOrderDTO.eventUuid())
                            .userUuid(userUuid)
                            .build();
                    return orderRepository.save(order)
                            .flatMap(savedOrder -> {
                                ticketsOrdered.forEach(ticketOrder -> ticketOrder.setOrderId(savedOrder.getId()));
                                return ticketOrderRepository.saveAll(ticketsOrdered)
                                        .collectList()
                                        .map(savedTicketOrder -> entityToDTO(savedOrder, (List<TicketOrder>) savedTicketOrder));
                            });
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<OrderDTO> getOrderByUuid(UUID uuid) {
        return orderRepository.findByUuid(uuid)
                .flatMap(orderFound ->
                        ticketOrderRepository.findByOrderId(orderFound.getId())
                                .collectList()
                                .map(tickerOrderList -> entityToDTO(orderFound, tickerOrderList)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("No order found with the uuid " + uuid)));
    }

    @Override
    public Flux<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .flatMap(orderFound ->
                        ticketOrderRepository.findByOrderId(orderFound.getId())
                                .collectList()
                                .map(ticketOrderList -> entityToDTO(orderFound, ticketOrderList))
                );
    }

    @Override
    @Transactional
    public Mono<OrderDTO> approveOrder(UUID uuid, UUID paymentUuid) {
        return orderRepository.findByUuid(uuid)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No order found with the id: " + uuid)))
                .flatMap(orderFound ->
                        ticketOrderRepository.findByOrderId(orderFound.getId())
                                .collectList()
                                .flatMap(ticketsOrderList -> {
                                    Mono<Void> discountTicketsCapacity = Flux.fromIterable(ticketsOrderList)
                                            .concatMap(ticketOrder -> eventWebClient.discountTicketCapacity(
                                                    ticketOrder.getTicketCategoryUuid(),
                                                    ticketOrder.getTicketQuantity()
                                            )).then();
                                    BigDecimal totalTickets = BigDecimal.valueOf(ticketsOrderList.stream().mapToDouble(TicketOrder::getTicketQuantity).sum());
                                    List<TicketDTO> createTicketsList = ticketsOrderList.stream()
                                            .flatMap(ticketOrder ->
                                                    IntStream.range(0, ticketOrder.getTicketQuantity()).mapToObj(i ->
                                                            new TicketDTO(
                                                                    paymentUuid,
                                                                    orderFound.getUserUuid(),
                                                                    orderFound.getUuid(),
                                                                    orderFound.getEventUuid(),
                                                                    ticketOrder.getTicketCategoryUuid(),
                                                                    ticketOrder.getTicketCategoryName()
                                                            )
                                                    )
                                            ).toList();
                                    log.info(createTicketsList.toString());
                                    System.out.println(createTicketsList);
                                    Mono<Void> createTickets = ticketClient.createTicket(createTicketsList)
                                            .timeout(Duration.ofSeconds(30));

                                    return discountTicketsCapacity
                                            .then(createTickets)
                                            .then(Mono.defer(() -> {
                                                orderFound.setOrderStatus(OrderStatus.APPROVED);
                                                return orderRepository.save(orderFound);
                                            }))
                                            .map(order -> entityToDTO(order, ticketsOrderList))
                                            .onErrorResume(WebClientResponseException.class, ex -> {
                                                if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                                                    orderFound.setOrderStatus(OrderStatus.REJECTED);
                                                    return orderRepository.save(orderFound)
                                                            .flatMap(_ -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Order Rejected: Insufficient ticket capacity")));
                                                }
                                                return Mono.error(ex);
                                            });

                                })
                );
    }

    @Override
    @Transactional
    public Mono<OrderDTO> rejectOrder(UUID uuid) {
        return orderRepository.findByUuid(uuid)
                .flatMap(orderFound -> {
                    orderFound.setOrderStatus(OrderStatus.REJECTED);
                    return orderRepository.save(orderFound);
                })
                .flatMap(orderFound ->
                        ticketOrderRepository.findByOrderId(orderFound.getId())
                                .collectList()
                                .map(ticketOrderList -> entityToDTO(orderFound, ticketOrderList))
                )
                .switchIfEmpty(Mono.error(new NoSuchElementException("No order found with the id: " + uuid)));
    }

    @Override
    public Flux<OrderDTO> getOrdersByUserUuid(UUID userUuid) {
        return orderRepository.findByUserUuid(userUuid)
                .flatMap(orderFound -> ticketOrderRepository.findByOrderId(orderFound.getId())
                        .collectList()
                        .map(ticketOrderList -> entityToDTO(orderFound, ticketOrderList)));
    }

    @Override
    public Mono<BigDecimal> getAmountFromOrder(UUID orderuuid) {
        return orderRepository.getTotalAmountFromOrder(orderuuid);
    }


    private OrderDTO entityToDTO(Order order, List<TicketOrder> ticketOrders) {
        return new OrderDTO(
                order.getUuid(),
                order.getEventUuid(),
                order.getUserUuid(),
                order.getTicketsOrdered(),
                order.getTotalAmount(),
                ticketOrders.stream().map(ticketOrder ->
                                new TicketOrderedDTO(
                                        ticketOrder.getUuid(),
                                        ticketOrder.getTicketCategoryUuid(),
                                        ticketOrder.getTicketCategoryName(),
                                        ticketOrder.getTicketQuantity(),
                                        ticketOrder.getTicketPrice()
                                ))
                        .collect(Collectors.toSet())
        );
    }

}
