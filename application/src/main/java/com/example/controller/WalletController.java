package com.example.controller;


import com.example.ports.api.WalletServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletServicePort walletServicePort;

    @PostMapping("/")
    public ResponseEntity createWallet(){

        return walletServicePort.createWallet();
    }

    @GetMapping("/wallets")
    public ResponseEntity getWallets(){
        return walletServicePort.getWallets();
    }
    @PostMapping("/addFunds")
    public ResponseEntity addFunds(Integer n, double amount){
        return walletServicePort.addFunds(n,amount);
    }

    @GetMapping("/balance/{n}")
    public ResponseEntity getBalance(@PathVariable Integer n){
        return walletServicePort.getBalance(n);
    }

    @PostMapping("/transferToWallet")
    public ResponseEntity transferToWallet(Integer n, String toAddress, double amount){
       return walletServicePort.transferToWallet(n,toAddress,amount);
    }

    @PostMapping("/history")
    public ResponseEntity getHistory(Integer n){
        return walletServicePort.getHistory(n);
    }

}
