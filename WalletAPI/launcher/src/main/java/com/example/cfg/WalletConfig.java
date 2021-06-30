package com.example.cfg;

import com.example.adapter.WalletJpaAdapter;
import com.example.ports.api.WalletServicePort;
import com.example.ports.spi.WalletPersistencePort;
import com.example.service.WalletServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletConfig {

    @Bean
    public WalletPersistencePort walletPersistence(){
        return new WalletJpaAdapter();
    }

    @Bean
    public WalletServicePort walletService(){
        return new WalletServiceImpl(walletPersistence());
    }
}

