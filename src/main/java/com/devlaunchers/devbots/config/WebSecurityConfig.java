package com.devlaunchers.devbots.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devlaunchers.devbots.authentication.impl.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity // (1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { // (1)

  @Override
  protected void configure(HttpSecurity http) throws Exception { // (2)
    http.authorizeRequests()
        .anyRequest()
        .authenticated() // (4)
        .and()
        .httpBasic()
        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .csrf()
        .disable(); // (7)
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("0xf2dd1727e45609575784d44993ed408eb361eb86")
        .password("{noop}password")
        .roles("USER");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
