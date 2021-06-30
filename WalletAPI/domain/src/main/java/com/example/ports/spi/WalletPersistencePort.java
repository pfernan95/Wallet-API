package com.example.ports.spi;

import org.springframework.http.ResponseEntity;

public interface WalletPersistencePort {

    ResponseEntity createWallet(Integer user_id);

    ResponseEntity addFunds(Integer id, double amount);

    ResponseEntity getBalance(Integer id);

    ResponseEntity getHistory(Integer id);

    ResponseEntity transferToWallet(Integer id, String address, double amount);

}
