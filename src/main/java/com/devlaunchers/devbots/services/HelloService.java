package com.devlaunchers.devbots.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Service;

@Service
@Path("/hello")
public class HelloService {
    
    @GET
    @Produces("text/plain")
    public String hello() {
	return "Hello from Spring";
    }
    
}
