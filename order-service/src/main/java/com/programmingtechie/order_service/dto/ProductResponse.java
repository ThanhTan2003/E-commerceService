package com.programmingtechie.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponse
{
    private String id;
    private String skuCode;
    private Boolean isExisting;
    private String name;
    private BigDecimal price;
    private String description;
}
