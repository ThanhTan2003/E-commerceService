package com.programmingtechie.inventory_service.controller;

import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.service.ImportHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/import_history")
@RequiredArgsConstructor
@Slf4j
public class ImportHistoryController
{
    private final ImportHistoryService importHistoryService;

    @GetMapping
    public List<ImportHistory> getAllImportHistories() {
        return importHistoryService.findAll();
    }
}
