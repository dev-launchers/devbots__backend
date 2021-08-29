package com.devlaunchers.devbots.services;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.devlaunchers.devbots.exceptions.BotPartNotFoundException;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<BotPartNotFoundException> {

  @Override
  public Response toResponse(BotPartNotFoundException ex) {
    return Response.status(Response.Status.NOT_FOUND).build();
  }
}
