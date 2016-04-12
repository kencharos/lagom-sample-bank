package sample.bank.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public final class Money {

  public final Long amount;

  @JsonCreator
  public Money(Long amount) {
    this.amount = Preconditions.checkNotNull(amount, "amount");
  }

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof Money && equalTo((Money) another);
  }

  private boolean equalTo(Money another) {
    return amount.equals(another.amount);
  }

  @Override
  public int hashCode() {
    int h = 31;
    h = h * 17 + amount.hashCode();
    return h;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("Money").add("amount", amount).toString();
  }
}
