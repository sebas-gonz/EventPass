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

@Table(name = "orders" , schema = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends BaseEntity {
    private @Id Long id;

    @Column("event_uuid")
    private UUID eventUuid;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("order_status")
    private OrderStatus orderStatus;

    @Column("tickets_ordered")
    private Integer ticketsOrdered;

    @Column("total_amount")
    private BigDecimal totalAmount;

}
