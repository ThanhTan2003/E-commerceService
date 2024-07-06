package com.programmingtechie.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "t_order")
public class Order
{
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String customerId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.orderDate = LocalDateTime.now();
    }
}
