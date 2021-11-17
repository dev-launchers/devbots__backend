package com.devlaunchers.devbots.authentication;

import org.pfj.lang.Option;
import org.pfj.lang.Result;
import org.springframework.security.core.userdetails.UserDetails;
import org.web3j.abi.datatypes.Address;

public interface UserProvider {
    
    public Option<UserDetails> getAuthenticatedUser();
    
    public Result<Address> getAuthenticatedUserWallet();
    
}
