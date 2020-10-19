package com.training360.security.service;

import com.training360.security.dto.AccountDTO;
import com.training360.security.model.Account;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Component
public class AccountDAO {

    private final DataSource dataSource;
    private final EntityManager entityManager;

    public AccountDAO(DataSource dataSource, EntityManager entityManager) {
        this.dataSource = dataSource;
        this.entityManager = entityManager;
    }

    public List<AccountDTO> findAccountsByCustomerIdDS(String customerId) {

        String sql = "select id,customer_id,name,balance from Account where customer_id = '%s'";

        try (
                Connection c = dataSource.getConnection();
                ResultSet rs = c.createStatement()
                        .executeQuery(String.format(sql, customerId))
        ) {
            List<AccountDTO> accounts = new ArrayList<>();
            while (rs.next()) {
                AccountDTO acc = AccountDTO.builder()
                        .id(rs.getLong("id"))
                        .customerId(rs.getString("customer_id"))
                        .name(rs.getString("name"))
                        .balance(rs.getLong("balance"))
                        .build();

                accounts.add(acc);
            }

            return accounts;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<AccountDTO> findAccountsByCustomerIdEM(String customerId) {
        String jql = "from Account where customerId = '%s'";
        TypedQuery<Account> q = entityManager.createQuery(String.format(jql, customerId), Account.class);
        return q.getResultList()
                .stream()
                .map(a -> AccountDTO.builder()
                        .id(a.getId())
                        .customerId(a.getCustomerId())
                        .balance(a.getBalance())
                        .name(a.getName())
                        .build())
                .collect(Collectors.toList());
    }

}
