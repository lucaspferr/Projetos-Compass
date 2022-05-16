package com.MS.shopstyle.config.security;

import com.MS.shopstyle.repository.CustomerRepository;
import com.MS.shopstyle.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class CostumerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomerUserDetailService customerUserDetailService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //Config de autenticacao, controle de acesso
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Config de Autorizacao (urls, endpoints)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET,"/v1/users").permitAll()
                .antMatchers(HttpMethod.POST,"/v1/users").permitAll()
                .antMatchers(HttpMethod.POST,"/v1/login").permitAll()
                .antMatchers(HttpMethod.GET,"/v1/users/*").permitAll()
                .antMatchers(HttpMethod.PUT,"/v1/users/*").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new CustomerAuthTokenFilter(authTokenService, customerRepository), UsernamePasswordAuthenticationFilter.class);
        //.and().httpBasic();
        //http
        //  .autorize as requisicoes
        //  .permitir as requisicoes GET com o padrao /v1/users
        //  .permitir as requisicoes POST com o padrao /v1/users
        //  .qlqr outra requisicao precisa ser autenticada
        //  .e o CSRF estara desativado
        //  .manejao de sessoes segue a padrao Stateless(qnd fizer autenticacao, n cria sessao)
        //  .e adicone o filtro q eu criei antes do filtro padrao
    }

    //Config de recursos estaticos(js, css, imagens, etc.)
    @Override
    public void configure(WebSecurity web) throws Exception {
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
