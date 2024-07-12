package com.programmingtechie.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ShipmentHistoryDto
{
    private String skuCode;
    private Integer quantity;
    private LocalDateTime date;
    private String note;
}
