package com.devlaunchers.devbots.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.devlaunchers.devbots.battles.BotBattleController;
import com.devlaunchers.devbots.botpart.BotPartController;

@Configuration
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
	register(BotPartController.class);
	register(BotBattleController.class);
    }
    
}
