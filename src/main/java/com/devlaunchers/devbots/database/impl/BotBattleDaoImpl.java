package com.devlaunchers.devbots.database.impl;

import static org.pfj.lang.Causes.cause;
import static org.pfj.lang.Result.failure;
import static org.pfj.lang.Result.success;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response.Status;

import org.pfj.lang.Causes;
import org.pfj.lang.Result;

import com.devlaunchers.devbots.battles.BotBattle;
import com.devlaunchers.devbots.database.BotBattleCustomRepository;

public class BotBattleDaoImpl implements BotBattleCustomRepository {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Result<BotBattle> findFirstUnprocessedBattle() {
    TypedQuery<BotBattle> query =
        entityManager
            .createQuery(
                "FROM BotBattle b WHERE b.defenderBotID = null ORDER BY battleID ASC",
                BotBattle.class)
            .setMaxResults(1);

    return executeQuery(query);
  }

  @Override
  public Result<BotBattle> findByID(BigInteger ID) {
    TypedQuery<BotBattle> query =
        entityManager
            .createQuery("FROM BotBattle b WHERE b.battleID = :id", BotBattle.class)
            .setParameter("id", ID)
            .setMaxResults(1);

    return executeQuery(query);
  }

  public <T> Result<T> executeQuery(TypedQuery<T> query) {
    try {
      return success(query.getSingleResult());
    } catch (NoResultException noResultException) {
      return failure(cause("No Result found!", Status.NOT_FOUND));
    } catch (Exception otherException) {
      return failure(Causes.fromThrowable(otherException));
    }
  }
}
