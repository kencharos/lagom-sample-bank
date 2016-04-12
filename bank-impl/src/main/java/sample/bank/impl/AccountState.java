package sample.bank.impl;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import sample.bank.api.Account;
import sample.bank.api.MoneyTransaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sample.bank.impl.AccountEntity.TransactonType;

/**
 */
@SuppressWarnings("serial")
@Immutable
@JsonDeserialize
public final class AccountState implements CompressedJsonable {

  public final Optional<Account> account;

  @JsonCreator
  public AccountState(Optional<Account> account) {
    this.account = Preconditions.checkNotNull(account, "account");
  }

  public AccountState addTransaction(MoneyTransaction tran) {
    Account a = account.get();

    long diff = 0;

    switch (TransactonType.valueOf(tran.type)) {
      case DEPOSIT: diff = tran.amount; break;
      case WITHDRAWAL: diff = -tran.amount; break;
    }
    // filter only today transaction.
    LocalDateTime today = LocalDateTime.now();
    Stream<MoneyTransaction> st = a.transactionsOfDay.plus(tran)
            .stream().filter(s -> tran.at.until(today, ChronoUnit.DAYS) == 0);

    PSequence<MoneyTransaction> newTrans = TreePVector.from(st.collect(Collectors.toList()));
    //System.out.println("account updated. balance=" + ( a.balance) +"diff" + diff + ", trans=" + newTrans.size());
    return new AccountState(Optional.of(new Account(a.id, a.name, a.balance + diff, Optional.of(newTrans))));


  }


  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof AccountState && equalTo((AccountState) another);
  }

  private boolean equalTo(AccountState another) {
    return account.equals(another.account);
  }

  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + account.hashCode();
    return h;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("FriendState").add("account", account).toString();
  }
}
