package com.example.ports.spi;

import com.example.data.JwtRequest;
import com.example.data.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserPersistencePort {

    ResponseEntity createUser(UserDTO userDTO);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    ResponseEntity createAuthenticationToken(JwtRequest authRequest) throws Exception;
}

