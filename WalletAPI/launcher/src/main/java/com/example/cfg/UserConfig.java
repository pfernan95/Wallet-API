package com.example.cfg;

import com.example.adapter.UserJpaAdapter;
import com.example.ports.api.UserServicePort;
import com.example.ports.spi.UserPersistencePort;
import com.example.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    public UserPersistencePort userPersistence(){
        return new UserJpaAdapter();
    }

    @Bean
    public UserServicePort userService(){
        return new UserServiceImpl(userPersistence());
    }

}
