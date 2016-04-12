package sample.bank.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.time.LocalDateTime;

@Immutable
@JsonDeserialize
public final class MoneyTransaction {

  public final String type;

  public final Long amount;

  public final LocalDateTime at;


  @JsonCreator
  public MoneyTransaction(String type, Long amount, LocalDateTime at) {
    this.type = Preconditions.checkNotNull(type, "type");
    this.amount = Preconditions.checkNotNull(amount, "amount");
    this.at = Preconditions.checkNotNull(at, "at");
  }

  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof MoneyTransaction && equalTo((MoneyTransaction) another);
  }

  private boolean equalTo(MoneyTransaction another) {
    return type.equals(another.type) && amount.equals(amount) && at.equals(at);
  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + amount.hashCode();
    result = 31 * result + at.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper("MoneyTransaction")
            .add("type",type).add("amount", amount).add("at", at).toString();
  }
}
