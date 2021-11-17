package com.devlaunchers.devbots.battles;

import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Option.option;
import static org.pfj.lang.Option.present;
import static org.pfj.lang.Result.lift;

import java.math.BigInteger;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Causes;
import org.pfj.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.bot.BotService;
import com.devlaunchers.devbots.database.BotBattleDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotBattleService {

  private final BotService botService;

  private final BotBattleDao botBattleDao;

  public Result<BotBattleBo> initBattle(Address player, BigInteger botID) {
    return botService
        .ownerOf(botID)
        .filter(
            cause("Bot '" + botID.toString() + "' doesn't belong to the Player", Status.FORBIDDEN),
            player::equals)
        .map(addr -> BotBattle.builder())
        .map(builder -> builder.attackingPlayer(player.toString()).attackerBotID(botID).build())
        .flatMap(
            botBattle ->
                option(botBattleDao.save(botBattle))
                    .toResult(cause("Unable to save Bot Battle", Status.INTERNAL_SERVER_ERROR)))
        .map(BotBattle::toBo);
  }

  @Scheduled(fixedRate = 500)
  @Transactional
  public void checkDB() {
    botBattleDao
        .findFirstUnprocessedBattle()
        .map(BotBattle::toBo)
        .map(
            b ->
                b.setDefendingPlayer(present(new Address("0xABCDEF")))
                    .setDefenderBotHealth(present(BigInteger.valueOf(32)))
                    .setDefenderBotID(present(BigInteger.valueOf(12))))
        .map(BotBattleBo::toEntity)
        .flatMap(botBattle -> lift(Causes::fromThrowable, () -> botBattleDao.save(botBattle)))
        .map(Object::toString)
        .onSuccess(log::info);
  }
}
