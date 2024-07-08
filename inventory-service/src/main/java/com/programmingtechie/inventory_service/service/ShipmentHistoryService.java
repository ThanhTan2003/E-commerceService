package com.programmingtechie.inventory_service.service;

import com.programmingtechie.inventory_service.dto.ShipmentHistoryDto;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import com.programmingtechie.inventory_service.repository.InventoryRepository;
import com.programmingtechie.inventory_service.repository.ShipmentHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentHistoryService
{
    final ShipmentHistoryRepository shipmentHistoryRepository;
    final InventoryRepository inventoryRepository;

    public List<ShipmentHistory> findAll() {
        return shipmentHistoryRepository.findAll();
    }

    public void createShipmentHistory(ShipmentHistoryDto shipmentHistoryDto)
    {
        ShipmentHistory shipmentHistory = ShipmentHistory.builder()
                .skuCode(shipmentHistoryDto.getSkuCode())
                .quantity(shipmentHistoryDto.getQuantity())
                .inventory(inventoryRepository.findBySkuCode(shipmentHistoryDto.getSkuCode()))
                .note(shipmentHistoryDto.getNote())
                .build();

        shipmentHistoryRepository.save(shipmentHistory);
    }
}
