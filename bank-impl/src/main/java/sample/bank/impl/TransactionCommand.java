package sample.bank.impl;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;

import akka.Done;

/**
 */
public interface TransactionCommand extends Jsonable {

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  // ReplyType<T> is return value of ComandHander
  public final class CreateAccount implements TransactionCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final String id;
    public final String name;

    @JsonCreator
    public CreateAccount(String id, String name) {

      this.id = Preconditions.checkNotNull(id, "id");
      this.name = Preconditions.checkNotNull(name, "name");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof CreateAccount && equalTo((CreateAccount) another);
    }

    private boolean equalTo(CreateAccount another) {
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
      return MoreObjects.toStringHelper("CreateAccount").add("id", id).toString();
    }
  }


  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class Deposit implements TransactionCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final Long amount;

    @JsonCreator
    public Deposit(Long amount) {

      this.amount = Preconditions.checkNotNull(amount, "amount");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof Deposit && equalTo((Deposit) another);
    }

    private boolean equalTo(Deposit another) {
      return amount.equals(amount);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + amount.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("Deposit").add("amount", amount).toString();
    }
  }

  @SuppressWarnings("serial")
  @Immutable
  @JsonDeserialize
  public final class Withdrawal implements TransactionCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
    public final Long amount;

    @JsonCreator
    public Withdrawal(Long amount) {

      this.amount = Preconditions.checkNotNull(amount, "amount");
    }

    @Override
    public boolean equals(@Nullable Object another) {
      if (this == another)
        return true;
      return another instanceof Withdrawal && equalTo((Withdrawal) another);
    }

    private boolean equalTo(Withdrawal another) {
      return amount.equals(amount);
    }

    @Override
    public int hashCode() {
      int h = 31;
      h = h * 17 + amount.hashCode();
      return h;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper("Deposit").add("amount", amount).toString();
    }
  }
}
