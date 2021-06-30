package com.example.ports.api;

import org.springframework.http.ResponseEntity;

public interface WalletServicePort {

    ResponseEntity createWallet();

    ResponseEntity getWallets();

    ResponseEntity addFunds(Integer n, double amount);

    ResponseEntity getBalance(Integer n);

    ResponseEntity getHistory(Integer n);

    ResponseEntity transferToWallet(Integer n, String address, double amount);
}