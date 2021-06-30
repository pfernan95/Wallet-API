package com.example.ports.api;

import com.example.data.JwtRequest;
import com.example.data.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserServicePort {

    ResponseEntity createUser(UserDTO userDTO);


    ResponseEntity createAuthenticationToken(JwtRequest authRequest) throws Exception;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
