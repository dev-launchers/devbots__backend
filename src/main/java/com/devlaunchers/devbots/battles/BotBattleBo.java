package com.devlaunchers.devbots.battles;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BotBattleBo {

  Address player;

  BigInteger botID;

  BigInteger gameID;

  BigInteger nonce;
}
