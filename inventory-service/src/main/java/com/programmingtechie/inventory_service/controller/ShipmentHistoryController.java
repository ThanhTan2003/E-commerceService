package com.programmingtechie.inventory_service.controller;

import com.programmingtechie.inventory_service.dto.InventoryRequest;
import com.programmingtechie.inventory_service.dto.ShipmentHistoryDto;
import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import com.programmingtechie.inventory_service.repository.ShipmentHistoryRepository;
import com.programmingtechie.inventory_service.service.ImportHistoryService;
import com.programmingtechie.inventory_service.service.ShipmentHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/shipment_history")
@RequiredArgsConstructor
@Slf4j
public class ShipmentHistoryController
{
    private final ShipmentHistoryService shipmentHistoryService;

    @GetMapping
    public List<ShipmentHistory> getAllShipmentHistories() {
        return shipmentHistoryService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createShipmentHistory(@RequestBody ShipmentHistoryDto shipmentHistoryDto) {
        shipmentHistoryService.createShipmentHistory(shipmentHistoryDto);
        return "Đã xuất hàng thành công!";
    }

}
