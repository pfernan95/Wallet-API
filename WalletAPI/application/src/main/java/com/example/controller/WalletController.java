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

    @PostMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createWallet(Integer user_id){

        return walletServicePort.createWallet(user_id);
    }

    @PostMapping("/addFunds")
    public ResponseEntity addFunds(Integer id, double amount){
        return walletServicePort.addFunds(id,amount);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity getBalance(@PathVariable Integer id){
        return walletServicePort.getBalance(id);
    }

    @PostMapping("/transferToWallet")
    public ResponseEntity transferToWallet(Integer id, String toAddress, double amount){
       return walletServicePort.transferToWallet(id,toAddress,amount);
    }

    @PostMapping("/history")
    public ResponseEntity getHistory(Integer id){
        return walletServicePort.getHistory(id);
    }

}
