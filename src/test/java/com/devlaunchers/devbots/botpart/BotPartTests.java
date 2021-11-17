package com.devlaunchers.devbots.botpart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.web3j.protocol.core.RemoteFunctionCall;

import com.devlaunchers.devbots.solidity.GameDatabase;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BotPartTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GameDatabase gameDatabase;

    private final String TEST_USERNAME = "0xf2dd1727e45609575784d44993ed408eb361eb86";

    private final String TEST_PASSWORD = "password";

  private <T> RemoteFunctionCall<T> createMockFunctionCall(T value) {
    RemoteFunctionCall<T> mock = Mockito.mock(RemoteFunctionCall.class);
    try {
      when(mock.sendAsync()).thenReturn(CompletableFuture.completedFuture(value));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mock;
  }

  @Test
  void testBotPartStat() throws Exception {
    BigInteger actualStat = BigInteger.valueOf(321);
    BigInteger botPartID = BigInteger.valueOf(1);
    BigInteger statID = BigInteger.valueOf(0);

    RemoteFunctionCall<BigInteger> callMock = createMockFunctionCall(actualStat);
    when(gameDatabase.getBotPartStat(botPartID, statID)).thenReturn(callMock);

    ResponseEntity<BotPartStat> entity =
        this.restTemplate.withBasicAuth(TEST_USERNAME, TEST_PASSWORD).getForEntity("/part/1/stat/0", BotPartStat.class);

    assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

    assertThat(entity.getBody().getStatValue()).isEqualTo(actualStat);
  }

}
