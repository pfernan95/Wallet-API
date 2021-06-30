package com.example.ports.api;

import org.springframework.http.ResponseEntity;

public interface WalletServicePort {

    ResponseEntity createWallet(Integer user_id);

    ResponseEntity addFunds(Integer id, double amount);

    ResponseEntity getBalance(Integer id);

    ResponseEntity getHistory(Integer id);

    ResponseEntity transferToWallet(Integer id, String address, double amount);
}