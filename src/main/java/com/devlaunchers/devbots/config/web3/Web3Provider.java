package com.devlaunchers.devbots.config.web3;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import com.devlaunchers.devbots.solidity.BotPart;
import com.devlaunchers.devbots.solidity.GameDatabase;

@Configuration
public class Web3Provider {

  private Logger logger = LoggerFactory.getLogger(Web3Provider.class);

  private Web3Config web3Config;

  private Web3j web3;

  private Credentials relayerCredentials;

  @Autowired
  public Web3Provider(Web3Config web3Config) {
    this.web3Config = web3Config;
    this.web3 = Web3j.build(new HttpService(web3Config.getProviderUrl()));
    try {
      this.relayerCredentials =
          WalletUtils.loadCredentials(web3Config.getWalletPassword(), web3Config.getWalletFile());
    } catch (IOException | CipherException e) {
      logger.error("Unable to load Wallet Credentials!", e);
    }
  }

  @Bean
  public Web3j getWeb3() {
    return web3;
  }

  @Bean
  public Credentials relayerCredentials() {
    return relayerCredentials;
  }

  @Bean
  @ConditionalOnProperty(
      name = "web3.gasProvider",
      havingValue = "org.web3j.tx.gas.StaticGasProvider")
  public ContractGasProvider staticGasProvider() {
    return new StaticGasProvider(
        Convert.toWei(BigDecimal.valueOf(web3Config.getStaticGas()), Unit.GWEI).toBigInteger(),
        BigInteger.valueOf(4_100_000_000L));
  }

  @Bean
  @Autowired
  public GameDatabase gameDatabase(
      Web3Contracts contracts,
      Web3j web3,
      Credentials credentials,
      ContractGasProvider gasProvider) {
    return GameDatabase.load(contracts.getGameDatabase(), web3, credentials, gasProvider);
  }

  @Bean
  @Autowired
  public BotPart botPart(
      Web3Contracts contracts,
      Web3j web3,
      Credentials credentials,
      ContractGasProvider gasProvider) {
    return BotPart.load(contracts.getBotPart(), web3, relayerCredentials, gasProvider);
  }
}
