package com.programmingtechie.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryRequest
{
    private List<ImportHistoryDto> importHistoryDtos;
}
