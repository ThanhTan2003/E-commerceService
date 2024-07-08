package com.programmingtechie.product_service.controller;

import com.programmingtechie.product_service.dto.ProductExisting;
import com.programmingtechie.product_service.dto.ProductRequest;
import com.programmingtechie.product_service.dto.ProductResponse;
import com.programmingtechie.product_service.repository.ProductRepository;
import com.programmingtechie.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest)
    {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct()
    {
        return productService.getAllProduct();
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateProduct(@PathVariable String id, @RequestBody ProductRequest productRequest) {
        productService.updateProduct(id, productRequest);
        return String.format("Product %s is updated", id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return String.format("Product %s is deleted", id);
    }

    @GetMapping("/existing")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductExisting> isExisting(@RequestParam List<String> skuCode) {

        return productService.isExisting(skuCode);
    }
}
