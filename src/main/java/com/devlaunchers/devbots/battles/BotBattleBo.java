package com.devlaunchers.devbots.battles;

import java.math.BigInteger;

import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Option;
import org.springframework.lang.NonNull;
import org.web3j.abi.datatypes.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotBattleBo {

  @NonNull BigInteger battleID;

  @NonNull BigInteger attackerBotID;

  Option<BigInteger> defenderBotID;

  @NonNull Address attackingPlayer;

  Option<Address> defendingPlayer;

  Option<BigInteger> attackerBotHealth;

  Option<BigInteger> defenderBotHealth;

  Option<String> transactionHash;

  public BotBattle toEntity() {
    return BotBattle.builder()
        .battleID(battleID)
        .attackerBotID(attackerBotID)
        .defenderBotID(defenderBotID.or((BigInteger) null))
        .attackingPlayer(attackingPlayer.toString())
        .defendingPlayer(defendingPlayer.map(Address::toString).or((String) null))
        .attackerBotHealth(attackerBotHealth.or((BigInteger) null))
        .defenderBotHealth(defenderBotHealth.or((BigInteger) null))
        .transactionHash(transactionHash.or((String) null))
        .build();
  }
}
