package com.programmingtechie.order_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderLineItemsDto
{
    private String id;

    private String skuCode;

    private String customer_id;

    private BigDecimal unitPrice;

    private Integer quantity;

    private BigDecimal totalAmount;

    private String note;
}
