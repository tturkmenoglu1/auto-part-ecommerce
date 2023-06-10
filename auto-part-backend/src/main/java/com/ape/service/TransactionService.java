package com.ape.service;

import com.ape.model.Transaction;
import com.ape.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void save (Transaction transaction){
        transactionRepository.save(transaction);
    }
}
