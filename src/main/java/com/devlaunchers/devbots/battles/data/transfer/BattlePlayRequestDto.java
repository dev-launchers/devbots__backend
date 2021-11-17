package com.devlaunchers.devbots.battles.data.transfer;

import java.io.Serializable;
import java.math.BigInteger;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattlePlayRequestDto implements Serializable {

  private static final long serialVersionUID = 347285534877790459L;

  @NotNull(message = "BotId is mandatory")
  private BigInteger botId;
}
