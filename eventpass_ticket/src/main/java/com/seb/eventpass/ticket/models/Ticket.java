package com.seb.eventpass.ticket.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "tickets")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Ticket extends BaseEntity{
    private @Id Long id;

    @Column("payment_uuid")
    private UUID paymentUuid;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("order_uuid")
    private UUID orderUuid;

    @Column("ticket_event_category_uuid")
    private UUID ticketEventCategoryUuid;

    @Column("event_uuid")
    private UUID eventUuid;

    @Column("ticket_code")
    private String ticketCode;

    @Column("is_used")
    @Builder.Default
    private Boolean isUsed = false;

    @Column("ticket_category_name")
    private String ticketCategoryName;
}
