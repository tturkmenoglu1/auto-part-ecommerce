package com.ape.service;

import com.ape.model.Payment;
import com.ape.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public void save(Payment payment){
        paymentRepository.save(payment);
    }
}
