package com.seb.eventpass.ticket.services;

import com.seb.eventpass.ticket.dto.request.CreateTicketDTO;
import com.seb.eventpass.ticket.dto.response.TicketDTO;
import com.seb.eventpass.ticket.models.Ticket;
import com.seb.eventpass.ticket.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public Mono<TicketDTO> createTicket(CreateTicketDTO createTicketDTO) {
        Ticket ticket = dtoToEntity(createTicketDTO);
        ticket.setTicketCode("TK" + UUID.randomUUID().toString().toUpperCase());
        return ticketRepository.save(ticket)
                .map(this::entityToDTO);
    }

    @Override
    public Flux<TicketDTO> createTickets(Flux<CreateTicketDTO> createTicketDTO) {
        return createTicketDTO.map(this::dtoToEntity)
                .map(ticket -> {
                    ticket.setTicketCode("TK" + UUID.randomUUID().toString().toUpperCase());
                    return ticket;
                })
                .collectList()
                .flatMapMany(ticketRepository::saveAll)
                .map(this::entityToDTO);
    }

    @Override
    public Mono<TicketDTO> getTicketByUuid(UUID uuid) {
        return ticketRepository.findByUuid(uuid)
                .map(this::entityToDTO);
    }

    @Override
    public Mono<TicketDTO> useTicket(UUID ticketUuid, String ticketCode) {
        return ticketRepository.findByUuid(ticketUuid)
                .switchIfEmpty(Mono.error(new NoSuchElementException("No ticket with de ID: " + ticketUuid)))
                .flatMap(ticketFound -> {
                    if (!ticketFound.getTicketCode().equals(ticketCode) || !ticketFound.getIsUsed()) {
                        return Mono.error(new IllegalArgumentException("The ticket code does not match with the ticket code: " + ticketCode));
                    }
                    ticketFound.setIsUsed(true);
                    return ticketRepository.save(ticketFound);
                }).map(this::entityToDTO);
    }

    @Override
    public Flux<TicketDTO> getTicketsByUserUuid(UUID userUuid) {
        return ticketRepository.findByUserUuid(userUuid)
                .map(this::entityToDTO);
    }

    private Ticket dtoToEntity(CreateTicketDTO createTicketDTO) {
        return Ticket.builder()
                .paymentUuid(createTicketDTO.paymentUuid())
                .userUuid(createTicketDTO.userUuid())
                .orderUuid(createTicketDTO.orderUuid())
                .eventUuid(createTicketDTO.eventUuid())
                .ticketCategoryName(createTicketDTO.ticketCategoryName())
                .build();
    }
    private TicketDTO entityToDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getUuid(),
                ticket.getUserUuid(),
                ticket.getOrderUuid(),
                ticket.getEventUuid(),
                ticket.getTicketCode(),
                ticket.getTicketEventCategoryUuid(),
                ticket.getIsUsed(),
                ticket.getTicketCategoryName()
        );
    }
}
