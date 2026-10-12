package com.seb.eventpass.eventos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "ticket_categories", schema = "events")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TicketCategory extends BaseEntity{
    private @Id Long id;

    @Column("category_name")
    private TicketsCategories categoryName;

    @Column("price")
    private BigDecimal price;

    @Column("event_id")
    private Long eventId;

    @Column("ticket_capacity")
    private Integer ticketCapacity;
}
