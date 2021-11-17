package com.devlaunchers.devbots.battles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.pfj.lang.Result.success;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.battles.data.transfer.BattlePlayRequestDto;
import com.devlaunchers.devbots.battles.data.transfer.BattlePlayRequestResponseDto;
import com.devlaunchers.devbots.battles.data.transfer.BotBattleDto;
import com.devlaunchers.devbots.data.transfer.StandardApiResponseDto;
import com.devlaunchers.devbots.database.BotBattleDao;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotBattleControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  @MockBean private BotBattleService botBattleService;

  @MockBean private BotBattleDao botBattleDao;

  private final String TEST_USERNAME = "0xf2dd1727e45609575784d44993ed408eb361eb86";

  private final String TEST_PASSWORD = "password";

  @Test
  void testBattleInit() throws Exception {
    BigInteger battleID = BigInteger.valueOf(32);

    when(botBattleService.initBattle(any(Address.class), any(BigInteger.class)))
        .then(
            (invocation) ->
                success(
                    BotBattleBo.builder()
                        .attackingPlayer(invocation.getArgument(0))
                        .attackerBotID(invocation.getArgument(1))
                        .battleID(battleID)
                        .build()));

    BigInteger botID = BigInteger.valueOf(421);

    BattlePlayRequestDto battlePlayRequest = new BattlePlayRequestDto(botID);

    ResponseEntity<BattlePlayRequestResponseDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .postForEntity("/battle/play", battlePlayRequest, BattlePlayRequestResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.ACCEPTED.getStatusCode());
    assertThat(entity.getBody().getBattleId()).isEqualTo(battleID);
  }

  @Test
  void testFailingBattleInit() throws Exception {
    ResponseEntity<StandardApiResponseDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .postForEntity("/battle/play", Map.of(), StandardApiResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
    assertThat(entity.getBody().getStatusMessage()).isEqualTo("Bot ID Parameter is needed");
  }

  @Test
  void testUnauthorizedBattleInit() {
    ResponseEntity<StandardApiResponseDto> entity =
        this.restTemplate.postForEntity("/battle/play", Map.of(), StandardApiResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.UNAUTHORIZED.getStatusCode());
    assertThat(entity.getBody().getStatusMessage()).isEqualTo("Unauthorized");
  }

  @Test
  void testBattleResult() throws Exception {
    BigInteger battleID = BigInteger.valueOf(23);
    BigInteger botID = BigInteger.valueOf(12);
    String player = "0xACDEFacd";

    BotBattle battle =
        BotBattle.builder().battleID(battleID).attackerBotID(botID).attackingPlayer(player).build();

    when(botBattleDao.findById(battleID)).thenReturn(Optional.of(battle));

    ResponseEntity<BotBattleDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .getForEntity("/battle/" + battleID.toString() + "/result", BotBattleDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.OK.getStatusCode());
    assertThat(entity.getBody()).isEqualTo(battle.toDto());
  }

  @Test
  void testFullBattleResult() throws Exception {
    BigInteger battleID = BigInteger.valueOf(23);
    String player = "0xACDEFacd";
    String defendingPlayer = "0xDEFA0121";

    BotBattle battle =
        BotBattle.builder()
            .battleID(battleID)
            .attackingPlayer(player)
            .defendingPlayer(defendingPlayer)
            .attackerBotID(BigInteger.valueOf(53))
            .defenderBotID(BigInteger.valueOf(92))
            .attackerBotHealth(BigInteger.valueOf(11))
            .defenderBotHealth(BigInteger.valueOf(76))
            .transactionHash("0x0123456789")
            .build();

    when(botBattleDao.findById(battleID)).thenReturn(Optional.of(battle));

    ResponseEntity<BotBattleDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .getForEntity("/battle/" + battleID.toString() + "/result", BotBattleDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.OK.getStatusCode());
    assertThat(entity.getBody()).isEqualTo(battle.toDto());
  }

  @Test
  void testNonExistingBattleResult() {
    BigInteger battleID = BigInteger.valueOf(12);

    when(botBattleDao.findById(any())).thenReturn(Optional.empty());

    ResponseEntity<StandardApiResponseDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .getForEntity("/battle/" + battleID + "/result", StandardApiResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
    assertThat(entity.getBody().getStatusMessage())
        .isEqualTo("BotBattle '" + battleID + "' does not exist!");
  }

  @Test
  void testUnauthorizedBattleResult() {
    ResponseEntity<StandardApiResponseDto> entity =
        this.restTemplate.getForEntity("/battle/12/result", StandardApiResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.UNAUTHORIZED.getStatusCode());
    assertThat(entity.getBody().getStatusMessage()).isEqualTo("Unauthorized");
  }

  @Test
  void testFailingBattleResult() {
    ResponseEntity<StandardApiResponseDto> entity =
        this.restTemplate
            .withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
            .getForEntity("/battle/-1/result", StandardApiResponseDto.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(entity.getBody().getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
    assertThat(entity.getBody().getStatusMessage()).isEqualTo("BattleID > 0");
  }
}
