package com.devlaunchers.devbots.botpart;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlaunchers.devbots.botpart.BotPartBo.BotPartBoBuilder;
import com.devlaunchers.devbots.gamedatabase.GameDatabaseService;
import com.devlaunchers.devbots.solidity.BotPart;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotPartService {

  private final BotPart botParts;
  private final GameDatabaseService gameDatabaseService;

  public CompletableFuture<BotPartBo> getBotPart(@NotNull BigInteger botPartID) {
    BotPartBoBuilder builder = BotPartBo.builder();
    builder.botPartID(botPartID);

    CompletableFuture<Void> requests =
        CompletableFuture.allOf(
            botParts.ownerOf(botPartID).sendAsync().thenAccept(builder::owner),
            botParts.getBotPartType(botPartID).sendAsync().thenAccept(builder::partType),
            getRawPartStats(botPartID).thenAccept(builder::rawStats),
            gameDatabaseService.getBotPartStats(botPartID).thenAccept(builder::stats));

    return requests.thenApplyAsync(future -> builder.build());
  }

  public CompletableFuture<BigInteger[]> getRawPartStats(@NotNull BigInteger botPartID) {
    List<CompletableFuture<BigInteger>> statRequests = new ArrayList<>();
    statRequests.add(getRawPartStat(botPartID, 0));
    statRequests.add(getRawPartStat(botPartID, 1));
    statRequests.add(getRawPartStat(botPartID, 2));
    statRequests.add(getRawPartStat(botPartID, 3));

    return CompletableFuture.allOf(statRequests.toArray(new CompletableFuture[0]))
        .thenApplyAsync(
            future ->
                statRequests.stream().map((CompletableFuture::join)).toArray(BigInteger[]::new));
  }

  public CompletableFuture<BigInteger> getRawPartStat(
      @NotNull BigInteger botPartID, @NotNull @Min(0) @Max(3) int statID) {
    return botParts.getRawTokenStat(botPartID, BigInteger.valueOf(statID)).sendAsync();
  }
}
