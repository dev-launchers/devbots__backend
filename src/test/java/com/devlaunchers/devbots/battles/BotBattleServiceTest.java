package com.devlaunchers.devbots.battles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Result.failure;
import static org.pfj.lang.Result.success;

import java.math.BigInteger;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pfj.lang.Cause;
import org.pfj.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.bot.BotService;
import com.devlaunchers.devbots.database.BotBattleDao;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotBattleServiceTest {

    @MockBean
    private BotService botService;

    @Autowired
    private BotBattleDao botBattleDao;

    @Autowired
    private BotBattleService botBattleService;

    @BeforeEach
    void clearDB() {
	botBattleDao.deleteAll();
    }

    @Test
    void testBattleInit() throws Exception {
	Address player = new Address("0x1234abcdbeef");
	BigInteger botID = BigInteger.valueOf(421);

	when(botService.ownerOf(botID)).thenReturn(success(player));

	Result<BotBattleBo> botBattle = botBattleService.initBattle(player, botID);

	assertThat(botBattle.isSuccess()).isTrue();

	BotBattleBo botBattleBo = botBattle.fold(null, battle -> battle);

	assertThat(botBattleBo.getAttackingPlayer()).isEqualTo(player);
	assertThat(botBattleBo.getAttackerBotID()).isEqualTo(botID);
	assertThat(botBattleBo.getBattleID()).isEqualTo(1);
	assertThat(botBattleBo.getDefenderBotID().isEmpty()).isTrue();
    }

    @Test
    void testForbiddenBattleInit() {
	Address player = new Address("0x1234abcdbeef");
	BigInteger botID = BigInteger.valueOf(12);

	when(botService.ownerOf(botID)).thenReturn(success(new Address("0x9876abcd1234")));

	Result<BotBattleBo> botBattle = botBattleService.initBattle(player, botID);

	assertThat(botBattle.isFailure()).isTrue();

	Cause cause = botBattle.fold(reason -> reason, null);

	assertThat(cause.statusCode()).isEqualTo(Status.FORBIDDEN);
    }

    @Test
    void testFailingBattleInit() {
	Address player = new Address("0x1234abcdbeef");
	BigInteger botID = BigInteger.valueOf(12);

	String testCauseMessage = "TEST_CAUSE";

	when(botService.ownerOf(botID)).thenReturn(failure(cause(testCauseMessage, Status.NOT_FOUND)));

	Result<BotBattleBo> botBattle = botBattleService.initBattle(player, botID);

	assertThat(botBattle.isFailure()).isTrue();

	Cause cause = botBattle.fold(reason -> reason, null);

	assertThat(cause.statusCode()).isEqualTo(Status.NOT_FOUND);
	assertThat(cause.message()).isEqualTo(testCauseMessage);
    }
    
    @Test
    void testIncorrectBotID() {
	Address player = new Address("0x1234abcdbeef");
	BigInteger botID = BigInteger.valueOf(2);

	BotBattleDao botBattleDaoMock = Mockito.mock(BotBattleDao.class);

	when(botService.ownerOf(botID)).thenReturn(success(player));

	when(botBattleDaoMock.save(Mockito.any(BotBattle.class))).thenReturn(null);
	
	BotBattleService botBattleService = new BotBattleService(botService, botBattleDaoMock);
	
	Result<BotBattleBo> botBattle = botBattleService.initBattle(player, botID);

	assertThat(botBattle.isFailure()).isTrue();

	Cause cause = botBattle.fold(reason -> reason, null);

	assertThat(cause.statusCode()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
    }
}
