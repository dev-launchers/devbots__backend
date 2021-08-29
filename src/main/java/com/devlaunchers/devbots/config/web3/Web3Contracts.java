package com.devlaunchers.devbots.config.web3;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class Web3Contracts {
  @NotNull
  @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Address specified for GameDatabase")
  private String gameDatabase;
  @NotNull
  @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Address specified for BotPart")
  private String botPart;
}
