package com.seb.eventpass.payment.controllers;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import com.seb.eventpass.payment.repositories.PaymentRepository;
import com.seb.eventpass.payment.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentWebController {
    private final PaymentService  paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<PaymentResponseDTO> createPayment(@Valid @RequestBody CreatePaymentDTO createPaymentDTO,
                                                  Principal principal){
        UUID userUuid = UUID.fromString(principal.getName());
        return paymentService.createPayment(createPaymentDTO, userUuid);
    }

    @GetMapping("/{paymentuuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Mono<PaymentResponseDTO> getPaymentByUuid(@PathVariable("paymentuuid") UUID paymentuuid){
        return paymentService.findPaymentByUuid(paymentuuid);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN' ,'USER')")
    public Flux<PaymentResponseDTO> getPaymentByUserUuid(Principal principal){
        UUID userUuid = UUID.fromString(principal.getName());
        return  paymentService.findPaymentByUserUuid(userUuid);
    }
}
