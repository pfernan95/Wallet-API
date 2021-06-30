package com.example.controller;

import com.example.data.JwtRequest;
import com.example.data.UserDTO;
import com.example.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserServicePort userServicePort;

    @PostMapping(value="/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(UserDTO userDTO) {

        return userServicePort.createUser(userDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity createAuthenticationToken(JwtRequest authRequest) throws Exception{
        return userServicePort.createAuthenticationToken(authRequest);
    }
}
