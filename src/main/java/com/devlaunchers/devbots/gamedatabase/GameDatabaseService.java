package com.devlaunchers.devbots.gamedatabase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devlaunchers.devbots.botpart.BotPartStat;
import com.devlaunchers.devbots.solidity.GameDatabase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Service
public class GameDatabaseService {

  private final GameDatabase gameDatabase;

  public CompletableFuture<BotPartStat> getBotPartStat(
      @NotNull BigInteger botPartID, @NotNull @Min(0) @Max(3) int statID) {
    return gameDatabase
        .getBotPartStat(botPartID, BigInteger.valueOf(statID))
        .sendAsync()
        .thenApplyAsync(BotPartStat::new);
  }

  public CompletableFuture<BotPartStat[]> getBotPartStats(@NotNull BigInteger botPartID) {
    List<CompletableFuture<BotPartStat>> statRequests = new ArrayList<>();
    statRequests.add(getBotPartStat(botPartID, 0));
    statRequests.add(getBotPartStat(botPartID, 1));
    statRequests.add(getBotPartStat(botPartID, 2));
    statRequests.add(getBotPartStat(botPartID, 3));

    return CompletableFuture.allOf(statRequests.toArray(new CompletableFuture[0]))
        .thenApplyAsync(
            future ->
                statRequests.stream().map((CompletableFuture::join)).toArray(BotPartStat[]::new));
  }
}
