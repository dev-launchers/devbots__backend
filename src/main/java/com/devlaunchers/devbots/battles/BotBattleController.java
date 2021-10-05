package com.devlaunchers.devbots.battles;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.devlaunchers.devbots.botpart.BotPartService;
import com.devlaunchers.devbots.gamedatabase.GameDatabaseService;

import lombok.RequiredArgsConstructor;

@Path("/battle")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotBattleController {}
