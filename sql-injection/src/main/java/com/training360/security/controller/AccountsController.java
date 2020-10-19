package com.training360.security.controller;

import com.training360.security.dto.AccountDTO;
import com.training360.security.service.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class AccountsController {

    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/ds/accounts")
    public List<AccountDTO> getAccountsDS(@RequestParam String customerId) {
        return accountDAO.findAccountsByCustomerIdDS(customerId);
    }

    @GetMapping("/em/accounts")
    public List<AccountDTO> getAccountsEM(@RequestParam String customerId) {
        return accountDAO.findAccountsByCustomerIdEM(customerId);
    }

}
