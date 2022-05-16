package com.MS.shopstyle.service;

import com.MS.shopstyle.model.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AuthTokenService {

    @Value("${customer.jwt.expiration}")
    private String expiration;

    @Value("${customer.jwt.secret}")
    private String secret;

    public String tokenGenerator(Authentication authentication) {
        Customer custLog = (Customer) authentication.getPrincipal();
        Date today = new Date();
        Date dateExpiration = new Date(today.getTime()+Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("API Customer Shop")
                .setSubject(custLog.getId().toString())
                .setIssuedAt(today)
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();

    }

    public boolean tokenValidation(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        }catch (Exception e){return false;}
    }

    public Long getIdCustomer(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
