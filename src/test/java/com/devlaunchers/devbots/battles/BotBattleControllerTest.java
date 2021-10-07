package com.devlaunchers.devbots.battles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.abi.datatypes.Address;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotBattleControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  @Mock private BotBattleService botBattleService;

  @Test
  void testBattleInit() throws Exception {
    BigInteger gameID = BigInteger.valueOf(32);
    BigInteger nonce = BigInteger.valueOf(5311);

    when(botBattleService.initBattle(any(Address.class), any(BigInteger.class)))
        .then((invocation) -> new BotBattleBo(invocation.getArgument(0), invocation.getArgument(1), gameID, nonce));

    Address player = new Address("0x1234abcdbeef");
    BigInteger botID = BigInteger.valueOf(421);

    ResponseEntity<BotBattleBo> entity =
        this.restTemplate.postForEntity(
            "/battle/init", Map.of("player", player, "botID", botID), BotBattleBo.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody().getPlayer()).isEqualTo(player);
    assertThat(entity.getBody().getBotID()).isEqualTo(botID);
    assertThat(entity.getBody().getGameID()).isEqualTo(gameID);
    assertThat(entity.getBody().getNonce()).isEqualTo(nonce);

    BigInteger nextGameID = BigInteger.valueOf(33);
    BigInteger nextNonce = BigInteger.valueOf(123453);

    when(botBattleService.initBattle(any(Address.class), any(BigInteger.class)))
        .then((invocation) -> new BotBattleBo(invocation.getArgument(0), invocation.getArgument(1), nextGameID, nextNonce));

    player = new Address("0xaaaddbeefa1276");
    botID = BigInteger.valueOf(752);

    entity =
        this.restTemplate.postForEntity(
            "/battle/init", Map.of("player", player, "botID", botID), BotBattleBo.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody().getPlayer()).isEqualTo(player);
    assertThat(entity.getBody().getBotID()).isEqualTo(botID);
    assertThat(entity.getBody().getGameID()).isEqualTo(nextGameID);
    assertThat(entity.getBody().getNonce()).isEqualTo(nextNonce);
  }

  @Test
  void testFailingBattleInit() throws Exception {
    when(botBattleService.initBattle(any(Address.class), any(BigInteger.class)))
        .then(
            (invocation) -> {
              if (((BigInteger) invocation.getArgument(1)).compareTo(BigInteger.ZERO) == -1) {
                throw new IllegalArgumentException();
              }
              return new BotBattleBo();
            });

    ResponseEntity<BotBattleBo> entity =
        this.restTemplate.postForEntity(
            "/battle/init",
            Map.of("player", new Address("0xabcdeffedcba"), "botID", BigInteger.valueOf(-2)),
            BotBattleBo.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }
}
