package com.devlaunchers.devbots.data.transfer;

import org.pfj.lang.Cause;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardApiResponseDto {

  private StandardApiResponseDto(Cause cause) {
    this.status = cause.statusCode().getStatusCode();
    this.statusMessage = cause.message();
  }

  @NonNull private Integer status;

  private String statusMessage;

  public static StandardApiResponseDto fromCause(Cause cause) {
    return new StandardApiResponseDto(cause);
  }
}
