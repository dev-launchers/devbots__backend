package com.devlaunchers.devbots.authentication.impl;

import static org.pfj.lang.Causes.cause;

import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Option;
import org.pfj.lang.Result;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Address;

import com.devlaunchers.devbots.authentication.UserProvider;

@Component
public class UserProviderImpl implements UserProvider {

  @Override
  public Option<UserDetails> getAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
      return Option.present(userPrincipal);
    }
    return Option.empty();
  }

  @Override
  public Result<Address> getAuthenticatedUserWallet() {
    return getAuthenticatedUser()
        .map(UserDetails::getUsername)
        .map(Address::new)
        .toResult(cause("Unauthorized", Status.UNAUTHORIZED));
  }
}
