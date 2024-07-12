package com.programmingtechie.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderResponse
{
    private List<OrderLineItemsDto> orderLineItemsDtos;

    private String id;

    private String customer_id;

    private String status;

    private BigDecimal totalAmount;

    private BigDecimal discount;

    private BigDecimal total;

    private String note;
}
