package com.seb.eventpass.orders.services.ticketorder;

import com.seb.eventpass.orders.dto.requests.CreateOrderTicketDTO;
import com.seb.eventpass.orders.dto.responses.TicketOrderedDTO;
import com.seb.eventpass.orders.dto.rest.TicketEventCategoryDTO;
import com.seb.eventpass.orders.models.TicketOrder;
import com.seb.eventpass.orders.repositories.OrderRepository;
import com.seb.eventpass.orders.repositories.TicketOrderRepository;
import com.seb.eventpass.orders.rest.EventClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TickerOrderServiceImpl implements  TicketOrderService{
    private final TicketOrderRepository ticketOrderRepository;
    private final OrderRepository orderRepository;
    private final EventClient eventWebClient;

    @Override
    public Mono<TicketOrderedDTO> createTicketCategory(CreateOrderTicketDTO createOrderTicketDTO, UUID eventId, Long orderId) {
        return eventWebClient.getTicketCategory(eventId, createOrderTicketDTO.ticketCategoryUuid())
                .flatMap(ticketCategory -> {
                    TicketOrder ticketOrder = dtoToEntity(createOrderTicketDTO, ticketCategory,  orderId);
                    return ticketOrderRepository.save(ticketOrder);
                }).map(this::entityToDTO);
    }

    @Override
    public Mono<TicketOrderedDTO> getTicketCategory(UUID uuid) {
        return ticketOrderRepository.findByUuid(uuid)
                .map(this::entityToDTO);
    }

    @Override
    public Flux<TicketOrderedDTO> getAllTicketCategories() {
        return ticketOrderRepository.findAll()
                .map(this::entityToDTO);
    }

    @Override
    public Flux<TicketOrderedDTO> getTicketCategoriesByOrderUuid(UUID orderUuid) {
        return orderRepository.findByUuid(orderUuid)
                .flatMapMany(order -> {
                    return ticketOrderRepository.findByOrderId(order.getId());
                })
                .map(this::entityToDTO);
    }

    public TicketOrder dtoToEntity(CreateOrderTicketDTO createOrderTicketDTO, TicketEventCategoryDTO ticketEventCategoryDTO, Long orderId) {
        return TicketOrder.builder()
                .orderId(orderId)
                .ticketQuantity(createOrderTicketDTO.ticketQuantity())
                .ticketCategoryUuid(ticketEventCategoryDTO.ticketCategoryUuid())
                .ticketCategoryName(ticketEventCategoryDTO.categoryName())
                .ticketPrice(ticketEventCategoryDTO.price())
                .build();
    }

    public TicketOrderedDTO entityToDTO(TicketOrder ticketOrder) {
        return new TicketOrderedDTO(
                ticketOrder.getUuid(),
                ticketOrder.getTicketCategoryUuid(),
                ticketOrder.getTicketCategoryName(),
                ticketOrder.getTicketQuantity(),
                ticketOrder.getTicketPrice()
        );
    }
}
