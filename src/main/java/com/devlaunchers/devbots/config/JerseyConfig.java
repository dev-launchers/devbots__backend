package com.devlaunchers.devbots.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.devlaunchers.devbots.services.HelloService;

@Configuration
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
	register(HelloService.class);
    }
    
}
