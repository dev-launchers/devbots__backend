package com.devlaunchers.devbots.botpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.protocol.core.RemoteFunctionCall;

import com.devlaunchers.devbots.botpart.BotPartStat;
import com.devlaunchers.devbots.solidity.GameDatabase;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotPartTests {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private GameDatabase gameDatabase;

  private <T> RemoteFunctionCall<T> createMockFunctionCall(T value) {
    RemoteFunctionCall<T> mock = Mockito.mock(RemoteFunctionCall.class);
    try {
      when(mock.send()).thenReturn(value);
    } catch (Exception e) {
	e.printStackTrace();
    }
    return mock;
  }

  @Test
  void hello() throws Exception {      
    BigInteger actualStat = BigInteger.valueOf(321);
    BigInteger botPartID = BigInteger.valueOf(1);
    BigInteger statID = BigInteger.valueOf(0);
    
    RemoteFunctionCall<BigInteger> callMock = createMockFunctionCall(actualStat);
    when(gameDatabase.getBotPartStat(botPartID, statID))
        .thenReturn(callMock);

    ResponseEntity<BotPartStat> entity = this.restTemplate.getForEntity("/part/1/stat/0", BotPartStat.class);
    
    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

    assertThat(entity.getBody().getStatValue()).isEqualTo(actualStat);
  }
}