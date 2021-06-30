package com.example.service;

import com.example.data.JwtRequest;
import com.example.data.UserDTO;
import com.example.ports.api.UserServicePort;
import com.example.ports.spi.UserPersistencePort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserServicePort {

    private UserPersistencePort userPersistencePort;

    public UserServiceImpl(UserPersistencePort userPersistencePort){
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public ResponseEntity createUser(UserDTO userDTO){
        return userPersistencePort.createUser(userDTO);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPersistencePort.loadUserByUsername(username);
    }

    @Override
    public ResponseEntity createAuthenticationToken(JwtRequest authRequest) throws Exception {
        return userPersistencePort.createAuthenticationToken(authRequest);
    }
}