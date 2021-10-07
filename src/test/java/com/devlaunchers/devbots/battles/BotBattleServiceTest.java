package com.devlaunchers.devbots.battles;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.abi.datatypes.Address;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotBattleServiceTest {

  @Autowired private BotBattleService botBattleService;

  @Test
  void testBattleInit() throws Exception {
    Address player = new Address("0x1234abcdbeef");
    BigInteger botID = BigInteger.valueOf(421);

    BotBattleBo botBattleBo = botBattleService.initBattle(player, botID);

    assertThat(botBattleBo.getPlayer()).isEqualTo(player);
    assertThat(botBattleBo.getBotID()).isEqualTo(botID);
    assertThat(botBattleBo.getGameID()).isPositive();
    assertThat(botBattleBo.getNonce()).isPositive();

    // TODO: Add Check for actual information in database
  }
}
