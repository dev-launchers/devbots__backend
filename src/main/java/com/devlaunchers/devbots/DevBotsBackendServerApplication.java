package com.devlaunchers.devbots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevBotsBackendServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DevBotsBackendServerApplication.class, args);
  }
}
