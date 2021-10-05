package com.devlaunchers.devbots.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.devlaunchers.devbots.solidity.GameDatabase;

@Profile("test")
@Configuration
public class GameDatabaseTestConfiguration {

  @Bean
  @Primary
  public GameDatabase testGameDatabase() {
    return Mockito.mock(GameDatabase.class);
  }
}
