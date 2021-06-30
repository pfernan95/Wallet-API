package com.example.service;

import com.example.ports.api.WalletServicePort;
import com.example.ports.spi.WalletPersistencePort;
import org.springframework.http.ResponseEntity;

public class WalletServiceImpl implements WalletServicePort {

    private WalletPersistencePort walletPersistencePort;

    public WalletServiceImpl(WalletPersistencePort walletPersistencePort){
        this.walletPersistencePort = walletPersistencePort;
    }

    @Override
    public ResponseEntity createWallet(Integer user_id){
        return walletPersistencePort.createWallet(user_id);
    }

    @Override
    public ResponseEntity addFunds(Integer id, double amount){
       return walletPersistencePort.addFunds(id,amount);
    }

    @Override
    public ResponseEntity getBalance(Integer id){
        return walletPersistencePort.getBalance(id);
    }

    @Override
    public ResponseEntity getHistory(Integer id){
        return walletPersistencePort.getHistory(id);
    }

    @Override
    public ResponseEntity transferToWallet(Integer id, String address, double amount){
        return walletPersistencePort.transferToWallet(id,address,amount);
    }
}
