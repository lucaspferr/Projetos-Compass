package com.MS.shopstyle.config.security;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.repository.CustomerRepository;
import com.MS.shopstyle.service.AuthTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CustomerAuthTokenFilter extends OncePerRequestFilter {

    private AuthTokenService authTokenService;

    private CustomerRepository customerRepository;

    public CustomerAuthTokenFilter(AuthTokenService authTokenService, CustomerRepository customerRepository) {
        this.authTokenService = authTokenService;
        this.customerRepository = customerRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = retrieveToken(request);

        boolean isTokenValid = authTokenService.tokenValidation(token);

        if(isTokenValid) authorizeCustomer(token);

        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){return null;}
        //7 pq 'Bearer ' ocupa 7 posicoes
        return token.substring(7, token.length());
    }

    private void authorizeCustomer(String token) {
        Long idCustomer = authTokenService.getIdCustomer(token);
        Customer customer = customerRepository.findById(idCustomer).get();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customer, null, customer.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
