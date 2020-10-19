package com.training360.security.controller;

import com.training360.security.data.OrderDTO;
import com.training360.security.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public Iterable<OrderDTO> findOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(o -> !o.getIsPrivate())
                .map(o ->
                        OrderDTO.builder()
                                .items(o.getItems())
                                .build()
                ).collect(Collectors.toList());
    }
}
