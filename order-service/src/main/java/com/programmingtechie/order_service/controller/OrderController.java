package com.programmingtechie.order_service.controller;

import com.programmingtechie.order_service.dto.OrderRequest;
import com.programmingtechie.order_service.dto.OrderResponse;
import com.programmingtechie.order_service.model.Order;
import com.programmingtechie.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController
{
    final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);

        // Sử dụng NumberFormat để định dạng với dấu phẩy phân cách phần nghìn
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setGroupingUsed(true);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        String totalAmountFormatted = numberFormat.format(orderResponse.getTotalAmount());
        String discountFormatted = numberFormat.format(orderResponse.getDiscount());
        String totalFormatted = numberFormat.format(orderResponse.getTotal());

        String output = String.format(
                "- Mã hóa đơn: %s\n" +
                "- Tổng tiền sản phẩm: %s\n" +
                "- Giảm giá: %s\n" +
                "[ TỔNG TIỀN: %s ]\n",
                orderResponse.getId(), totalAmountFormatted, discountFormatted, totalFormatted);

        return "Đã đặt hàng thành công!\n" +
                "-------------------------------------------------------------\n" +
                output +
                "-------------------------------------------------------------\n";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Order> orderDetails(@PathVariable String id)
    {
        return orderService.orderDetails(id);
    }

}
