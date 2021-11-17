package com.devlaunchers.devbots.authentication.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.devlaunchers.devbots.data.transfer.StandardApiResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");

    response
        .getOutputStream()
        .println(
            objectMapper.writeValueAsString(
                StandardApiResponseDto.builder()
                    .status(Status.UNAUTHORIZED.getStatusCode())
                    .statusMessage("Unauthorized")
                    .build()));
  }
}
