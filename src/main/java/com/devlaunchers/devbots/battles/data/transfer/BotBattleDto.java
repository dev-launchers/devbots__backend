package com.devlaunchers.devbots.battles.data.transfer;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import com.devlaunchers.devbots.data.transfer.StandardApiResponseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class BotBattleDto extends StandardApiResponseDto {

  public BotBattleDto() {
    super(Response.Status.OK.getStatusCode());
  }

  private BigInteger battleID;

  private BigInteger attackerBotID;

  private BigInteger defenderBotID;

  private String attackingPlayer;

  private String defendingPlayer;

  private BigInteger attackerBotHealth;

  private BigInteger defenderBotHealth;

  private String transactionHash;
}
