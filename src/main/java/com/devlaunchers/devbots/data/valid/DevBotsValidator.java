package com.devlaunchers.devbots.data.valid;

import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Result.lift;

import java.util.Set;

import javax.inject.Singleton;
import javax.validation.Validator;
import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Causes;
import org.pfj.lang.Result;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Singleton
@Service
public class DevBotsValidator {

  private final Validator validator;

  public <T> Result<T> validate(T object, String errorMsg) {
    return lift(Causes::fromThrowable, () -> validator.validate(object))
        .filter(cause(errorMsg, Status.BAD_REQUEST), Set::isEmpty)
        .fold(Result::failure, b -> Result.success(object));
  }

  public <T> Result<T> validate(T object) {
    return validate(object, "Invalid Object of Class '" + object.getClass().getName() + "'");
  }
}
