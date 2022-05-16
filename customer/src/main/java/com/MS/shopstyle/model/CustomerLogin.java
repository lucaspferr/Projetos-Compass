package com.MS.shopstyle.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CustomerLogin {


    private String email;
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordAuthenticationToken dataConversor() {
        return new UsernamePasswordAuthenticationToken(email,password);
    }
}

