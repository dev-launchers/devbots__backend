package com.devlaunchers.devbots.database;

import java.math.BigInteger;

import org.pfj.lang.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlaunchers.devbots.battles.BotBattle;

@Repository
public interface BotBattleDao extends JpaRepository<BotBattle, BigInteger>, BotBattleCustomRepository {

}
