package com.training360.security.config;

import com.training360.security.model.Order;
import com.training360.security.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class Fixtures {

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void setUp() {

        Order o0 = Order.builder()
                .isPrivate(false)
                .items("pen, phone, glass")
                .build();
        Order o1 = Order.builder()
                .isPrivate(false)
                .items("pencil, paper, ball")
                .build();
        Order o2 = Order.builder()
                .isPrivate(true)
                .items("beer")
                .build();

        orderRepository.save(o0);
        orderRepository.save(o1);
        orderRepository.save(o2);

    }
}
