package com.programmingtechie.inventory_service.service;

import com.programmingtechie.inventory_service.dto.*;
import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import com.programmingtechie.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService
{
    final InventoryRepository inventoryRepository;
    final ImportHistoryService importHistoryService;
    final ShipmentHistoryService shipmentHistoryService;
    final WebClient.Builder webClientBuilder;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        log.info("Đang kiểm tra tồn kho");

        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);

        return inventories.stream()
                .map(inventory -> {
                    boolean inStock = inventory.getQuantity() > 0;

                    if (!inStock) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm " + inventory.getSkuCode() + " không có sẵn trong kho");
                    }

                    return InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(true)
                            .build();
                })
                .toList();
    }

    public void createInventory(InventoryRequest inventoryRequest) {
        List<ImportHistory> importHistories = inventoryRequest.getImportHistoryDtos()
                .stream()
                .map(importHistoryDto -> ImportHistory.builder()
                        .skuCode(importHistoryDto.getSkuCode())
                        .quantity(importHistoryDto.getQuantity())
                        .note(importHistoryDto.getNote())
                        .build())
                .toList();

        List<String> skuCodes = importHistories.stream()
                .map(ImportHistory::getSkuCode)
                .toList();

        ProductResponse[] productResponses = webClientBuilder.build().get()
                .uri("http://product-service/api/product/existing",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();

        if (productResponses == null) {
            throw new IllegalArgumentException("Không thể kiểm tra sản phẩm.");
        }

        for (ProductResponse productResponse : productResponses) {
            if (!productResponse.isExisting()) {
                throw new IllegalArgumentException("Mã sản phẩm " + productResponse.getSkuCode() + " không tồn tại.");
            }
        }

        for (ImportHistory importHistory : importHistories) {
            Inventory existingInventory = inventoryRepository.findBySkuCode(importHistory.getSkuCode());
            if (existingInventory != null) {
                existingInventory.setQuantity(existingInventory.getQuantity() + importHistory.getQuantity());
                importHistory.setInventory(existingInventory); // Thiết lập thuộc tính inventory
                existingInventory.getImportHistories().add(importHistory);
                inventoryRepository.save(existingInventory);
            } else {
                Inventory newInventory = Inventory.builder()
                        .skuCode(importHistory.getSkuCode())
                        .quantity(importHistory.getQuantity())
                        .importHistories(List.of(importHistory))
                        .build();
                importHistory.setInventory(newInventory); // Thiết lập thuộc tính inventory
                inventoryRepository.save(newInventory);
            }

            importHistoryService.save(importHistory);
        }
    }


    public List<Inventory> findAll()
    {
        return inventoryRepository.findAll();
    }

    public Inventory findBySkuCode(String skuCode)
    {
        return inventoryRepository.findBySkuCode(skuCode);
    }

    public void save(Inventory inventory)
    {
        inventoryRepository.save(inventory);
    }


    public void updateQuantity(String skuCode, Integer quantity)
    {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);

        if (inventory == null) {
            throw new IllegalArgumentException("Không tìm thấy mã SkuCode trong kho");
        }

        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng trong kho không đủ");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
        ShipmentHistoryDto shipmentHistory = ShipmentHistoryDto.builder()
                .skuCode(skuCode)
                .quantity(quantity)
                .build();
        shipmentHistoryService.createShipmentHistory(shipmentHistory);
    }
}
