package sample.bank.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@JsonDeserialize
public final class Transfer {

  public final String to;

  public final Long amount;


  @JsonCreator
  public Transfer(String to, Long amount) {
    this.to = Preconditions.checkNotNull(to, "to");
    this.amount = Preconditions.checkNotNull(amount, "amount");
  }

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof Transfer && equalTo((Transfer) another);
  }

  private boolean equalTo(Transfer another) {
    return amount.equals(another.amount) && to.equals(another.to);
  }

  @Override
  public int hashCode() {
    int result = to.hashCode();
    result = 31 * result + amount.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("Money").add("amount", amount).toString();
  }
}
