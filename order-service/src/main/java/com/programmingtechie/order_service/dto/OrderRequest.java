package com.programmingtechie.order_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest
{
    private List<OrderLineItemsDto> orderLineItemsDtos;

    private String customer_id;

    private String status;

    private BigDecimal totalAmount;

    private BigDecimal discount;

    private BigDecimal total;

    private String note;
}
