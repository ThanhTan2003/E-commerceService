package com.programmingtechie.inventory_service.service;

import com.programmingtechie.inventory_service.dto.*;
import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.model.Inventory;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import com.programmingtechie.inventory_service.repository.InventoryRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
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
                    return InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inStock)
                            .quantity(inventory.getQuantity()) // trả về số lượng tồn kho
                            .build();
                })
                .toList();
    }

    public List<ProductResponse> isProductExist(List<String> skuCodes)
    {
        ProductResponse [] productResponses = webClientBuilder.build().get()
                .uri("http://product-service/api/product/existing",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(ProductResponse[].class)
                .block();

        for (ProductResponse productResponse : productResponses) {
            if (!productResponse.getIsExisting()) {
                throw new IllegalStateException(
                        String.format("Sản phẩm có mã skuCode %s không tồn tại. Vui lòng kiểm tra lại!",
                                productResponse.getSkuCode()));
            }
        }
        return Arrays.stream(productResponses).toList();
    }

    public void createInventory(InventoryRequest inventoryRequest)
    {
        List<String> skuCodes = inventoryRequest.getImportHistoryDtos().stream()
                .map(
                    ImportHistoryDto::getSkuCode
                ).toList();

        List<ProductResponse> productResponses = isProductExist(skuCodes);

        Integer index = 0;

        for(ImportHistoryDto importHistoryDto:inventoryRequest.getImportHistoryDtos())
        {
            if(inventoryRepository.findBySkuCode(importHistoryDto.getSkuCode()) == null)
            {
                Inventory inventory = Inventory.builder()
                        .skuCode(importHistoryDto.getSkuCode())
                        .quantity(importHistoryDto.getQuantity())
                        .build();
                inventoryRepository.save(inventory);
            }
            else
            {
                Inventory inventory = inventoryRepository.findBySkuCode(importHistoryDto.getSkuCode());
                Integer quantity = inventory.getQuantity();

                inventory.setQuantity(quantity + importHistoryDto.getQuantity());

                inventoryRepository.save(inventory);
            }
            ImportHistory importHistory = ImportHistory.builder()
                    .skuCode(importHistoryDto.getSkuCode())
                    .name(productResponses.get(index).getName())
                    .quantity(importHistoryDto.getQuantity())
                    .note(importHistoryDto.getNote())
                    .build();
            importHistoryService.save(importHistory);

            index++;
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


    @Transactional
    public void updateQuantity(ShipmentHistoryDto shipmentHistoryDto) {
        Inventory inventory = inventoryRepository.findBySkuCode(shipmentHistoryDto.getSkuCode());

        if (inventory == null) {
            throw new IllegalArgumentException("Không tìm thấy mã SkuCode trong kho");
        }

        Integer quantity = shipmentHistoryDto.getQuantity();
        if (quantity <= 0 || inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng trong kho không đủ hoặc số lượng không hợp lệ");
        }

        // Cập nhật số lượng tồn kho
        inventory.setQuantity(inventory.getQuantity() - quantity);

        try {
            inventoryRepository.save(inventory);
        } catch (OptimisticLockException e) {
            throw new IllegalStateException("Có sự thay đổi về dữ liệu kho trong khi xử lý. Vui lòng thử lại.");
        }

        // Lưu thông tin lịch sử xuất hàng
        List<String> skuCodes = Collections.singletonList(shipmentHistoryDto.getSkuCode());
        ProductResponse productResponse = isProductExist(skuCodes).get(0);

        BigDecimal totalPrice = productResponse.getPrice().multiply(BigDecimal.valueOf(quantity));
        ShipmentHistoryDto shipmentHistory = ShipmentHistoryDto.builder()
                .skuCode(shipmentHistoryDto.getSkuCode())
                .name(productResponse.getName())
                .quantity(quantity)
                .unitPrice(productResponse.getPrice())
                .totalPrice(totalPrice)
                .note(shipmentHistoryDto.getNote())
                .build();

        shipmentHistoryService.createShipmentHistory(shipmentHistory);
    }
}
