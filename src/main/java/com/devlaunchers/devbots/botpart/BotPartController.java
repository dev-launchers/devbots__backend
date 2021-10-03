package com.devlaunchers.devbots.botpart;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.devlaunchers.devbots.exceptions.BotPartNotFoundException;
import com.devlaunchers.devbots.gamedatabase.GameDatabaseService;

import lombok.RequiredArgsConstructor;

@Path("/part")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotPartController {

  private final BotPartService botPartService;

  private final GameDatabaseService gameDatabaseService;

  @GET
  @Path("/{id}")
  @Produces("application/json")
  public BotPartBo getBotPart(@PathParam("id") BigInteger botPartID) throws Exception {
    return botPartService.getBotPart(botPartID).get();
  }

  @GET
  @Path("/{id}/stat/{stat}")
  @Produces("application/json")
  public BotPartStat getActualBotPartStat(
      @PathParam("id") BigInteger botPartID, @PathParam("stat") int stat)
      throws BotPartNotFoundException, InterruptedException, ExecutionException {
    return gameDatabaseService.getBotPartStat(botPartID, stat).get();
  }
}
