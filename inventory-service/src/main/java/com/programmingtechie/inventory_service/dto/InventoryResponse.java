package com.programmingtechie.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryResponse
{
    private String skuCode;
    private boolean isInStock;
    private Integer quantity;
}
