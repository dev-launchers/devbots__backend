package com.devlaunchers.devbots.config.web3;

import java.io.File;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
@Component
@ConfigurationProperties("web3")
public class Web3Config {

  @NotNull private String providerUrl;
  @NotNull private File walletFile;
  @NotNull private String walletPassword;

  @NotNull private String gasProvider;
  private int staticGas = 2;
  
  private Web3Contracts contracts;

  @Bean
  public Web3Contracts getContracts() {
      return contracts;
  }
}
