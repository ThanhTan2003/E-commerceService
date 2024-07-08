package com.programmingtechie.order_service.service;


import com.programmingtechie.order_service.dto.InventoryResponse;
import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.event.OrderPlacedEvent;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.model.OrderLineItems;
import com.programmingtechie.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService
{
    final OrderRepository orderRepository;
    final WebClient.Builder webClientBuilder;
    //final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest)
    {
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(orderLineItemsDto -> OrderLineItems.builder()
                        .skuCode(orderLineItemsDto.getSkuCode())
                        .price(orderLineItemsDto.getPrice())
                        .quantity(orderLineItemsDto.getQuantity())
                        .build())
                .toList();

        List<String> skuCodes = orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseArray;

        try {
            inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory/is_in_stock",
                            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
        } catch (Exception e) {
            throw new IllegalStateException("Dịch vụ kiểm tra kho hàng không khả dụng. Vui lòng thử lại sau.");
        }

        if (inventoryResponseArray == null) {
            throw new IllegalArgumentException("Không thể lấy thông tin kho hàng.");
        }

        boolean allProductIsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if (!allProductIsInStock) {
            throw new IllegalArgumentException("Sản phẩm không có sẵn trong kho.");
        }

        Order order = Order.builder()
                .orderLineItems(orderLineItems)
                .build();

        orderRepository.save(order);

        updateInventory(orderLineItems);

        return "Đặt hàng thành công";
    }

    private void updateInventory(List<OrderLineItems> orderLineItems) {
        orderLineItems.forEach(item -> {
            webClientBuilder.build().post()
                    .uri("http://inventory-service/api/inventory/update_quantity",
                            uriBuilder -> uriBuilder.queryParam("skuCode", item.getSkuCode())
                                    .queryParam("quantity", item.getQuantity()).build())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        });
    }
}
