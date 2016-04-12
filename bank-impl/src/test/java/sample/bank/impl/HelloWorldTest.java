package sample.bank.impl;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import sample.bank.impl.TransactionCommand.Hello;
import sample.bank.impl.TransactionCommand.UseGreetingMessage;
import sample.bank.impl.TransactionEvent.GreetingMessageChanged;

public class HelloWorldTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create("HelloWorldTest");
  }

  @AfterClass
  public static void teardown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void testHelloWorld() {
    PersistentEntityTestDriver<TransactionCommand, TransactionEvent, AccountState> driver = new PersistentEntityTestDriver<>(system,
        new AccountEntity(), "world-1");

    Outcome<TransactionEvent, AccountState> outcome1 = driver.run(new Hello("Alice", Optional.empty()));
    assertEquals("Hello, Alice!", outcome1.getReplies().get(0));
    assertEquals(Collections.emptyList(), outcome1.issues());

    Outcome<TransactionEvent, AccountState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
        new Hello("Bob", Optional.empty()));
    assertEquals(1, outcome2.events().size());
    assertEquals(new GreetingMessageChanged("Hi"), outcome2.events().get(0));
    assertEquals("Hi", outcome2.state().message);
    assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
    assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
    assertEquals(2, outcome2.getReplies().size());
    assertEquals(Collections.emptyList(), outcome2.issues());
  }

}
