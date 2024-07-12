package com.programmingtechie.inventory_service.service;

import com.programmingtechie.inventory_service.dto.ImportHistoryDto;
import com.programmingtechie.inventory_service.dto.ShipmentHistoryDto;
import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import com.programmingtechie.inventory_service.repository.ImportHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportHistoryService {
    final ImportHistoryRepository importHistoryRepository;

    public List<ImportHistory> findAll()
    {
        return importHistoryRepository.findAll();
    }

    public void save(ImportHistory importHistory)
    {
        importHistoryRepository.save(importHistory);
    }

    public void createImportHistory(ImportHistoryDto importHistoryDto)
    {
        ImportHistory importHistory = ImportHistory.builder()
                .skuCode(importHistoryDto.getSkuCode())
                .quantity(importHistoryDto.getQuantity())
                .note(importHistoryDto.getNote())
                .build();

        importHistoryRepository.save(importHistory);
    }
}
