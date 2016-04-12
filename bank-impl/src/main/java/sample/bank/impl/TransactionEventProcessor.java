package sample.bank.impl;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;


import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletionStage;

/**
 */
public class TransactionEventProcessor extends CassandraReadSideProcessor<TransactionEvent> {

    // initialized once
    private PreparedStatement writeAccount;
    private PreparedStatement writeOffset;
    private PreparedStatement updateAccount;
    private PreparedStatement writeHistory;


    @Override
    public AggregateEventTag<TransactionEvent> aggregateTag() {
        return TransactionEventTag.INSTANCE;
    }

    @Override
    public CompletionStage<Optional<UUID>> prepare(CassandraSession session) {
        // prepare read side tables, statement and get event offset.
        // @formatter:off
        return prepareCreateTables(session).thenCompose(a ->
               prepareWriteAccount(session).thenCompose(b ->
               prepareWriteHistory(session).thenCompose(c ->
               prepareUpdateAccount(session).thenCompose(d ->
               prepareWriteOffset(session).thenCompose(e ->
               selectOffset(session))))));
        // @formatter:on
    }

    private CompletionStage<Done> prepareCreateTables(CassandraSession session) {
        // @formatter:off
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS account ("
                        + "account_id text, name text, balance bigint, "
                        + "PRIMARY KEY (account_id))")
                .thenCompose(a -> session.executeCreateTable(
                 "CREATE TABLE IF NOT EXISTS transaction_history ("
                     + "account_id text, at timestamp, amount bigint, type text, "
                     + "PRIMARY KEY (account_id, at))")
                .thenCompose(b -> session.executeCreateTable(
                      "CREATE TABLE IF NOT EXISTS account_offset ("
                           + "partition int, offset timeuuid, "
                           + "PRIMARY KEY (partition))")));
        // @formatter:on
    }

    private CompletionStage<Done> prepareWriteAccount(CassandraSession session) {
        return session.prepare("INSERT INTO account (account_id, name, balance) VALUES (?, ?, 0)").thenApply(ps -> {
            this.writeAccount = ps;
            return Done.getInstance();
        });
    }

    private CompletionStage<Done> prepareWriteOffset(CassandraSession session) {
        // when duplicate key, data is updated , and don't raise error in cassandra.
        // account_offset table always has only one latest offset record, because this table pk is partition only.
        return session.prepare("INSERT INTO account_offset (partition, offset) VALUES (1,?)").thenApply(ps -> {
            this.writeOffset = ps;
            return Done.getInstance();
        });
    }

    private CompletionStage<Done> prepareUpdateAccount(CassandraSession session) {
        return session.prepare("UPDATE account set balance = ? where account_id = ?").thenApply(ps -> {
            this.updateAccount = ps;
            return Done.getInstance();
        });
    }

    private CompletionStage<Done> prepareWriteHistory(CassandraSession session) {
        return session.prepare("INSERT INTO transaction_history (account_id, at, amount,type) VALUES (?, ?, ?, ?)").thenApply(ps -> {
            this.writeHistory = ps;
            return Done.getInstance();
        });
    }

    private CompletionStage<Optional<UUID>> selectOffset(CassandraSession session) {
        return session.selectOne("SELECT offset FROM account_offset")
                .thenApply(optionalRow -> {
                    Optional<UUID> uuid =  optionalRow.map(r -> r.getUUID("offset"));
                    if (uuid.isPresent()) {
                        System.out.println("prepare uuid ->" + uuid.get().toString());
                    } else {
                        System.out.println("prepare uuid is none.");
                    }
                    return uuid;
                });
    }


    @Override
    public EventHandlers defineEventHandlers(EventHandlersBuilder builder) {
        // when Account created, insert account table;
        builder.setEventHandler(TransactionEvent.AccountCreated.class, (ev, offset) ->{
            System.out.println("offset ->" + offset);
            BoundStatement st = writeAccount.bind()
                    .setString("account_id", ev.id)
                    .setString("name", ev.name);


            BoundStatement stOffset = writeOffset.bind(offset);

            return completedStatements(Arrays.asList(st, stOffset));
    });
        // when Deposit, insert history and update balance
        builder.setEventHandler(TransactionEvent.MoneyDeposited.class, (ev, offset) ->{
            System.out.println("offset ->" + offset);
            BoundStatement historyInsert = writeHistory.bind()
                    .setString("account_id", ev.id)
                    .setLong("amount",ev.amount)
                    .setString("type", "DEPOSIT")
                    .setTimestamp("at", toTimestamp(offset));

            BoundStatement accountUpdate = updateAccount.bind()
                    .setString("account_id", ev.id)
                    .setLong("balance", ev.balance + ev.amount);

            return completedStatements(Arrays.asList(historyInsert, accountUpdate, writeOffset.bind(offset)));
        });

        // when Withdrawal, insert history and update balance
        builder.setEventHandler(TransactionEvent.MoneyWithdrawn.class, (ev, offset) ->{
            System.out.println("offset ->" + offset);
            BoundStatement historyInsert = writeHistory.bind()
                    .setString("account_id", ev.id)
                    .setLong("amount", ev.amount)
                    .setString("type", "WITHDRAWAL")
                    .setTimestamp("at", toTimestamp(offset));

            BoundStatement accountUpdate = updateAccount.bind()
                    .setString("account_id", ev.id)
                    .setLong("balance", ev.balance - ev.amount);

            return completedStatements(Arrays.asList(historyInsert, accountUpdate, writeOffset.bind(offset)));
        });
        return builder.build();
    }


    /** UUID type1 timestampe to unix epoch Timestamp */
    private Timestamp toTimestamp(UUID type1UUID) {

        final long  NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
        return new Timestamp((type1UUID.timestamp()-NUM_100NS_INTERVALS_SINCE_UUID_EPOCH)/10000L);
    }
}
