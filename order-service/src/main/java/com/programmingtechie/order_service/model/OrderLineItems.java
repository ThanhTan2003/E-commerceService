package com.programmingtechie.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "order_line_items")
public class OrderLineItems
{
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String skuCode;

    @Column(length = 36)
    private String customer_id;

    private BigDecimal price;

    private Integer quantity;

    @PrePersist
    private void ensureId()
    {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
