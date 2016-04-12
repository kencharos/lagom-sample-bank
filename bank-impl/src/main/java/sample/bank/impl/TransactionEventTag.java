package sample.bank.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

/**
 *
 */
public class TransactionEventTag {
    public static final AggregateEventTag<TransactionEvent> INSTANCE =
            AggregateEventTag.of(TransactionEvent.class);

}
