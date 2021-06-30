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
    public ResponseEntity createWallet(){
        return walletPersistencePort.createWallet();
    }

    @Override
    public ResponseEntity getWallets() {
        return walletPersistencePort.getWallets();
    }
    @Override
    public ResponseEntity addFunds(Integer n, double amount){
       return walletPersistencePort.addFunds(n,amount);
    }

    @Override
    public ResponseEntity getBalance(Integer n){
        return walletPersistencePort.getBalance(n);
    }

    @Override
    public ResponseEntity getHistory(Integer n){
        return walletPersistencePort.getHistory(n);
    }

    @Override
    public ResponseEntity transferToWallet(Integer n, String address, double amount){
        return walletPersistencePort.transferToWallet(n,address,amount);
    }
}
