package sample.bank.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import org.pcollections.PSequence;

import java.util.Optional;

/**
 */
public interface BankService extends Service {


  // command
  ServiceCall<NotUsed, Account, NotUsed> createAccount();

  ServiceCall<String, Money, NotUsed> deposit();

  ServiceCall<String, Money, NotUsed> withdrawal();

  ServiceCall<String, Transfer, NotUsed> transfer();

  // query
  ServiceCall<String, NotUsed, Optional<Account>> getAccount();
  ServiceCall<String, NotUsed, PSequence<MoneyTransaction>> getHistory();

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("bankService").with(
        restCall(Method.POST, "/api/account", createAccount()),
        restCall(Method.GET, "/api/account/:id", getAccount()),
        restCall(Method.GET, "/api/account/:id/history", getHistory()),
        restCall(Method.PUT,  "/api/account/:id/deposit", deposit()),
        restCall(Method.PUT,  "/api/account/:id/withdrawal", withdrawal()),
        restCall(Method.PUT,  "/api/account/:id/transfer", transfer())
      ).withAutoAcl(true);
    // @formatter:on
  }
}
