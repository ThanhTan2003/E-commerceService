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
    final KafkaTemplate <String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest)
    {
        Order order = new Order();


        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(orderLineItemsDto -> OrderLineItems.builder()
                    .skuCode(orderLineItemsDto.getSkuCode())
                    .price(orderLineItemsDto.getPrice())
                    .quantity(orderLineItemsDto.getQuantity())
                    .build()
                ).toList();

        order.setOrderLineItems(orderLineItems);

        List <String> skuCodes = order.getOrderLineItems()
                .stream()
                .map(
                        OrderLineItems::getSkuCode
                ).toList();

        // Khoi tao WebClient va gui yeu cau GET
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        assert inventoryResponseArray != null;
        boolean allProductIsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(
                        InventoryResponse::isInStock
                );

        if(allProductIsInStock)
        {
            orderRepository.save(order);
            //kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed successfully";
        }
        else
            throw new IllegalArgumentException("Product is not in stock, please try again later");
    }
}
