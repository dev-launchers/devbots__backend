package com.devlaunchers.devbots.bot;

import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Result.lift;

import java.math.BigInteger;

import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.solidity.BotHull;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotService {

  private final BotHull botHull;

  public Result<Address> ownerOf(BigInteger botID) {
    return lift(tr -> cause(tr.getMessage(), Status.NOT_FOUND), () -> botHull.ownerOf(botID).send())
        .map(Address::new);
  }
}
