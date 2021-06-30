package com.example.entity;


import com.example.data.WalletHistoryDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "wallet_history")
public class WalletHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Europe/Madrid")
    @Column(name="transfer_date")
    private Date transfer_date;

    @Column(name="amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    @OnDelete(action =  OnDeleteAction.CASCADE)
    @JsonIgnore
    private Wallet wallet;

    public WalletHistory(){}

    public WalletHistory(Integer id){
        this.id = id;
    }

    public WalletHistory(WalletHistoryDTO walletHistoryDTO){
        this.transfer_date = Timestamp.valueOf(walletHistoryDTO.getTransfer_date());
        this.amount = walletHistoryDTO.getAmount();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getTransfer_date() {
        return transfer_date;
    }

    public void setTransfer_date(Date transfer_date) {
        this.transfer_date = transfer_date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
