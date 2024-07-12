package com.programmingtechie.order_service.repository;

import com.programmingtechie.order_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findById(String id);
}
