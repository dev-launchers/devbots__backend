package com.devlaunchers.devbots.battles.data.transfer;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import com.devlaunchers.devbots.data.transfer.StandardApiResponseDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class BattlePlayRequestResponseDto extends StandardApiResponseDto {

  public BattlePlayRequestResponseDto(BigInteger battleId) {
    super(Response.Status.ACCEPTED.getStatusCode());

    this.battleId = battleId;
  }

  @JsonCreator
  public BattlePlayRequestResponseDto(
      @JsonProperty("battleId") BigInteger battleId, @JsonProperty("status") int status) {
    super(status);

    this.battleId = battleId;
  }

  private final BigInteger battleId;
}
