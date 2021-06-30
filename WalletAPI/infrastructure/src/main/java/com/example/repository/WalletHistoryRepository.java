package com.example.repository;


import com.example.entity.Wallet;
import com.example.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {

    List<WalletHistory> findByWalletId(Integer wallet_id);
    Optional<Wallet> findByIdAndWalletId(Integer id, Integer wallet_id);
}
