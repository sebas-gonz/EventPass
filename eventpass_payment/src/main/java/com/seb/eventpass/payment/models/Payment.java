package com.seb.eventpass.payment.models;

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

@Table(name = "payment", schema = "payment")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity{
    private @Id Long id;

    @Column("user_uuid")
    private UUID userUuid;

    @Column("order_uuid")
    private UUID orderUuid;

    @Column("card_number")
    private String cardNumber;

    @Column("card_holder")
    private String cardHolder;

    private String cvv;

    private BigDecimal amount;

    @Column("payment_status")
    private PaymentStatus paymentStatus;
}
