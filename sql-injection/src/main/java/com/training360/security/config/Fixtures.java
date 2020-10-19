package com.training360.security.config;

import com.training360.security.model.Account;
import com.training360.security.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Fixtures {

    private final AccountRepository accountRepository;

    public Fixtures(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void createAccounts() {
        accountRepository.save(Account.builder()
                .name("Robert Rich")
                .customerId("rich")
                .balance(78945131L)
                .build());
        accountRepository.save(Account.builder()
                .name("Pete Namlook")
                .customerId("namlook")
                .balance(135748L)
                .build());
        accountRepository.save(Account.builder()
                .name("Brian williams")
                .customerId("lustmord")
                .balance(35498745L)
                .build());
    }
}
