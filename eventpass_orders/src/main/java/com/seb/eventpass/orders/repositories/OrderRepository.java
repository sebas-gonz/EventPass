package com.seb.eventpass.orders.repositories;

import com.seb.eventpass.orders.models.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Mono<Order> findByUuid(UUID uuid);

    Flux<Order> findByUserUuid(UUID userUuid);

    @Query(value = "SELECT total_amount FROM ORDERS  WHERE uuid = :orderUuid")
    Mono<BigDecimal> getTotalAmountFromOrder(@Param("orderUuid")  UUID orderUuid);
}
