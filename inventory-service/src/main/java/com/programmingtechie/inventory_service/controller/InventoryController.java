package com.programmingtechie.inventory_service.controller;

import com.programmingtechie.inventory_service.dto.InventoryRequest;
import com.programmingtechie.inventory_service.dto.InventoryResponse;
import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController
{
    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    public List<Inventory> getAllInventories() {
        return inventoryService.findAll();
    }


    @GetMapping("/is_in_stock")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        log.info("Received inventory check request for skuCode: {}", skuCode);
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping("/update_quantity")
    @ResponseStatus(HttpStatus.OK)
    public void updateQuantity(@RequestParam String skuCode, @RequestParam Integer quantity)
    {
        inventoryService.updateQuantity(skuCode, quantity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody InventoryRequest request) {
        inventoryService.createInventory(request);
        return "Đã nhập số lượng sản phẩm thành công!";
    }
}
