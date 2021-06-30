package com.example.entity;

import com.example.data.WalletDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@DynamicInsert
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="balance",columnDefinition = "double default 0")
    private Double balance; //Default


    @Column(name="address",nullable = false)
    private String address;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "wallet")
    private Set<WalletHistory> walletHistorySet;


    public Wallet(){}

    public Wallet(Integer id){
        this.id = id;
    }

    public Wallet(WalletDTO walletDTO){

        this.address = walletDTO.getAddress();
        this.balance = walletDTO.getBalance();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<WalletHistory> getWalletHistorySet() {
        return walletHistorySet;
    }

    public void setWalletHistorySet(Set<WalletHistory> walletHistorySet) {
        this.walletHistorySet = walletHistorySet;
    }
}
