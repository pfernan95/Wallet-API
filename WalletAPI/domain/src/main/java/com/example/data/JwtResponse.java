package com.example.data;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class JwtResponse extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtResponse(String token){
        super(null,null);
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
