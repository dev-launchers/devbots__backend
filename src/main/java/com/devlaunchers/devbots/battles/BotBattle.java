package com.devlaunchers.devbots.battles;

import static org.pfj.lang.Option.option;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.ws.rs.core.Response.Status;

import org.springframework.lang.NonNull;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.battles.data.transfer.BotBattleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Accessors(chain = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotBattle {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  BigInteger battleID;

  @NonNull
  @Column(nullable = false)
  BigInteger attackerBotID;

  BigInteger defenderBotID;

  @NonNull
  @Column(nullable = false)
  String attackingPlayer;

  String defendingPlayer;

  BigInteger attackerBotHealth;

  BigInteger defenderBotHealth;

  String transactionHash;

  public BotBattleDto toDto() {
    return BotBattleDto.builder()
        .battleID(battleID)
        .attackerBotID(attackerBotID)
        .defenderBotID(defenderBotID)
        .attackingPlayer(attackingPlayer)
        .defendingPlayer(defendingPlayer)
        .attackerBotHealth(attackerBotHealth)
        .defenderBotHealth(defenderBotHealth)
        .transactionHash(transactionHash)
        .status(Status.OK.getStatusCode())
        .build();
  }

  public BotBattleBo toBo() {
    return BotBattleBo.builder()
        .battleID(battleID)
        .attackerBotID(attackerBotID)
        .defenderBotID(option(defenderBotID))
        .attackingPlayer(new Address(attackingPlayer))
        .defendingPlayer(option(defendingPlayer).map(Address::new))
        .attackerBotHealth(option(attackerBotHealth))
        .defenderBotHealth(option(defenderBotHealth))
        .transactionHash(option(transactionHash))
        .build();
  }
}
