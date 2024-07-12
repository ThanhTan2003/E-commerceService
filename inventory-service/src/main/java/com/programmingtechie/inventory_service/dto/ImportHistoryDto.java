package com.programmingtechie.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImportHistoryDto
{
    private String skuCode;
    private String name;
    private Integer quantity;
    private LocalDateTime date;
    private String note;
}
