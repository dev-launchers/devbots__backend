package com.devlaunchers.devbots.utils;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.pfj.lang.Cause;

import com.devlaunchers.devbots.data.transfer.StandardApiResponseDto;

public class StandardStatusResponse {

  public static Response fromCause(Cause cause) {
    return Response.status(cause.statusCode())
        .entity(StandardApiResponseDto.fromCause(cause))
        .type(MediaType.APPLICATION_JSON)
        .build();
  }

  public static Response ok(StandardApiResponseDto apiResponse) {
    return Response.ok(apiResponse, MediaType.APPLICATION_JSON).build();
  }

  public static Response accepted(StandardApiResponseDto apiResponse) {
    return Response.accepted(apiResponse).type(MediaType.APPLICATION_JSON).build();
  }
}
