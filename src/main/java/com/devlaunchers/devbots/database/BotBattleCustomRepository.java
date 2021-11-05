package com.devlaunchers.devbots.database;

import java.math.BigInteger;

import org.pfj.lang.Result;

import com.devlaunchers.devbots.battles.BotBattle;

public interface BotBattleCustomRepository {
  public Result<BotBattle> findFirstUnprocessedBattle();

  public Result<BotBattle> findByID(BigInteger ID);
}
