package com.seb.eventpass.orders.models;

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

@Table(name = "ticket_order")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TicketOrder extends BaseEntity{
    private @Id Long id;

    @Column("ticket_category_uuid")
    private UUID ticketCategoryUuid;

    @Column("ticket_category_name")
    private String ticketCategoryName;

    @Column("ticket_quantity")
    private Integer ticketQuantity;

    @Column("ticket_price")
    private BigDecimal ticketPrice;

    @Column("order_id")
    private Long orderId;
}
