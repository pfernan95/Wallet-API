package com.example.repository;

import com.example.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet findByAddress(String address);
    List<Wallet> findByUserId(Integer user_id);
    Optional<Wallet> findByIdAndUserId(Integer id, Integer user_id);
}