package com.seb.eventpass.payment.controllers;

import com.seb.eventpass.payment.dto.request.CreatePaymentDTO;
import com.seb.eventpass.payment.dto.response.PaymentResponseDTO;
import com.seb.eventpass.payment.repositories.PaymentRepository;
import com.seb.eventpass.payment.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentWebController {
    private final PaymentService  paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PaymentResponseDTO> createPayment(@RequestBody CreatePaymentDTO createPaymentDTO){
        return paymentService.createPayment(createPaymentDTO);
    }

    @GetMapping("/{paymentuuid}")
    public Mono<PaymentResponseDTO> getPaymentByUuid(@PathVariable("paymentuuid") UUID paymentuuid){
        return paymentService.findPaymentByUuid(paymentuuid);
    }
}
