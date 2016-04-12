package sample.bank.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import sample.bank.api.BankService;
import sample.bank.api.BankService;

public class BankServiceModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindServices(serviceBinding(BankService.class, BankServiceImpl.class));
  }
}
