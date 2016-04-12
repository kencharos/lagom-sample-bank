package sample.bank.impl;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.serialization.Jsonable;

/**
 *
 */
public interface TransactionEvent extends Jsonable ,AggregateEvent<TransactionEvent>{

  /** For query  */
  @Override
  default AggregateEventTag<TransactionEvent> aggregateTag() {
    return TransactionEventTag.INSTANCE;
  }

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class AccountCreated implements TransactionEvent {
    public final String id;
    public final String name;

    @JsonCreator
    public AccountCreated(String id,String name) {

      this.id = Preconditions.checkNotNull(id, "id");
      this.name = Preconditions.checkNotNull(name, "name");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof AccountCreated && equalTo((AccountCreated) another);
    }

    private boolean equalTo(AccountCreated another) {
      return id.equals(another.id);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + id.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("AccountCreated").add("id", id).toString();
    }
  }

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class MoneyDeposited implements TransactionEvent {
    public final String id;
    public final Long amount;
    public final Long balance;

    @JsonCreator
    public MoneyDeposited(String id, Long amount, Long balance) {

      this.id = Preconditions.checkNotNull(id, "id");
      this.amount = Preconditions.checkNotNull(amount, "amount");
      this.balance = Preconditions.checkNotNull(balance, "balance");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof MoneyDeposited && equalTo((MoneyDeposited) another);
    }

    private boolean equalTo(MoneyDeposited another) {
      return id.equals(id) && amount.equals(amount)  && balance.equals(balance);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + id.hashCode();
      h = h * 17 + amount.hashCode();
      h = h * 17 + balance.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("MoneyDeposited").add("amount", amount).toString();
    }
  }


  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class MoneyWithdrawn implements TransactionEvent {
    public final String id;
    public final Long amount;
    public final Long balance;

    @JsonCreator
    public MoneyWithdrawn(String id, Long amount, Long balance) {

      this.id = Preconditions.checkNotNull(id, "id");
      this.amount = Preconditions.checkNotNull(amount, "amount");
      this.balance = Preconditions.checkNotNull(balance, "balance");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof MoneyWithdrawn && equalTo((MoneyWithdrawn) another);
    }

    private boolean equalTo(MoneyWithdrawn another) {
      return id.equals(id) && amount.equals(amount) && balance.equals(balance);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + id.hashCode();
      h = h * 17 + amount.hashCode();
      h = h * 17 + balance.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("MoneyWithdrawn").add("amount", amount).toString();
    }
  }
}
