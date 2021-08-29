package com.devlaunchers.devbots.botpart;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BotPartBo {

  private final BigInteger botPartID;
  private final String owner;
  private final BigInteger partType;
  private final BigInteger[] rawStats;
  private final BotPartStat[] stats;
}
