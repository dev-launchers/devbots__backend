package com.devlaunchers.devbots.battles;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class BotBattleService {

  public BotBattleBo initBattle(Address player, BigInteger botID) {
    return new BotBattleBo();
  }
}
