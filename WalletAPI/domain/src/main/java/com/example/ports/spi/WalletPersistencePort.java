package com.example.ports.spi;

import org.springframework.http.ResponseEntity;

public interface WalletPersistencePort {

    ResponseEntity createWallet();

    ResponseEntity getWallets();

    ResponseEntity addFunds(Integer n, double amount);

    ResponseEntity getBalance(Integer n);

    ResponseEntity getHistory(Integer n);

    ResponseEntity transferToWallet(Integer n, String address, double amount);

}
