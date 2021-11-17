package com.devlaunchers.devbots.battles;

import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Result.success;

import java.math.BigInteger;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Option;
import org.pfj.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.devlaunchers.devbots.authentication.UserProvider;
import com.devlaunchers.devbots.battles.data.transfer.BattlePlayRequestDto;
import com.devlaunchers.devbots.battles.data.transfer.BattlePlayRequestResponseDto;
import com.devlaunchers.devbots.data.valid.DevBotsValidator;
import com.devlaunchers.devbots.database.BotBattleDao;
import com.devlaunchers.devbots.utils.StandardStatusResponse;

import lombok.RequiredArgsConstructor;

@Path("battle")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Singleton
public class BotBattleController {

  private final BotBattleService botBattleService;

  private final BotBattleDao botBattleDao;

  private final UserProvider userProvider;

  private final DevBotsValidator devBotsValidator;

  @POST
  @Path("play")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response playBotBattle(BattlePlayRequestDto battlePlayRequestDto) {
    return Result.all(
            devBotsValidator
                .validate(battlePlayRequestDto, "Bot ID Parameter is needed")
                .map(BattlePlayRequestDto::getBotId),
            userProvider.getAuthenticatedUserWallet())
        .flatMap((botId, address) -> botBattleService.initBattle(address, botId))
        .map(BotBattleBo::getBattleID)
        .map(BattlePlayRequestResponseDto::new)
        .fold(StandardStatusResponse::fromCause, StandardStatusResponse::accepted);
  }

  @GET
  @Path("{battleID}/result")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBotBattleResult(@PathParam("battleID") BigInteger battleID) {
    return success(battleID)
        .filter(cause("BattleID > 0", Status.BAD_REQUEST), id -> id.compareTo(BigInteger.ZERO) > 0)
        .flatMap(
            id ->
                Option.from(botBattleDao.findById(id))
                    .toResult(
                        cause("BotBattle '" + battleID + "' does not exist!", Status.NOT_FOUND)))
        .map(BotBattle::toDto)
        .fold(StandardStatusResponse::fromCause, StandardStatusResponse::ok);
  }
}
