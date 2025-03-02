package com.programmingtechie.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    @Column(length = 36)
    private String customerId;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(length = 100)
    private String status;

    private BigDecimal totalAmount;

    private BigDecimal discount;

    private BigDecimal total;

    private String note;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.orderDate == null) {
            // Lấy thời gian hiện tại ở múi giờ UTC+7
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Bangkok"));
            this.orderDate = zonedDateTime.toLocalDateTime();
        }
    }
}
