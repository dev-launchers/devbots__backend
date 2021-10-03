package com.devlaunchers.devbots.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.devlaunchers.devbots.botpart.BotPartController;
import com.devlaunchers.devbots.services.NotFoundExceptionMapper;

@Configuration
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
	register(BotPartController.class);
	register(NotFoundExceptionMapper.class);
    }
    
}
