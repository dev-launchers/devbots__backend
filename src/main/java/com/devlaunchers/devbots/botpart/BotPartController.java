package com.devlaunchers.devbots.botpart;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.devlaunchers.devbots.gamedatabase.GameDatabaseService;

import lombok.RequiredArgsConstructor;

@Path("/part")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Singleton
public class BotPartController {

  private final BotPartService botPartService;

  private final GameDatabaseService gameDatabaseService;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public BotPartBo getBotPart(@PathParam("id") BigInteger botPartID) throws Exception {
    return botPartService.getBotPart(botPartID).get();
  }

  @GET
  @Path("/{id}/stat/{stat}")
  @Produces(MediaType.APPLICATION_JSON)
  public BotPartStat getActualBotPartStat(
      @PathParam("id") BigInteger botPartID, @PathParam("stat") int stat)
      throws InterruptedException, ExecutionException {
    return gameDatabaseService.getBotPartStat(botPartID, stat).get();
  }
}
