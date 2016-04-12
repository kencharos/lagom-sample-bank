package sample.bank.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/** Bank Account */
@Immutable
@JsonDeserialize
public final class Account {

  public final String id;

  public final String name;

  public final Long balance;

  public final PSequence<MoneyTransaction> transactionsOfDay;

  @JsonCreator
  public Account(String id, String name) {
    this(id, name, 0L,Optional.empty());
  }

  public Account(String id, String name, Long balance, Optional<PSequence<MoneyTransaction>> trans) {
    this.id = Preconditions.checkNotNull(id, "id");
    this.name = Preconditions.checkNotNull(name, "name");
    this.balance = Preconditions.checkNotNull(balance, "balance");
    this.transactionsOfDay = trans.orElse(TreePVector.empty());
  }


  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another)
      return true;
    return another instanceof Account && equalTo((Account) another);
  }

  private boolean equalTo(Account another) {
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
    return MoreObjects.toStringHelper("Account").add("id", id)
            .add("name", name).add("balance", balance).toString();
  }
}
