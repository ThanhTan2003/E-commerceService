package com.programmingtechie.product_service.service;

import com.programmingtechie.product_service.dto.ProductExisting;
import com.programmingtechie.product_service.dto.ProductRequest;
import com.programmingtechie.product_service.dto.ProductResponse;
import com.programmingtechie.product_service.model.Product;
import com.programmingtechie.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j                      // Dùng để ghi log
public class ProductService {

    final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest)
    {
        Product product = Product.builder()
                .skuCode(productRequest.getSkuCode())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
        .build();

        productRepository.save(product);

        // Ghi log
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProduct()
    {
        List<Product> products = productRepository.findAll();

        return products.stream().map( product -> ProductResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build()
        ).toList();
    }

    public void updateProduct(String id, ProductRequest productRequest)
    {
        // Tìm sản phẩm hiện có theo ID
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            // Lấy sản phẩm hiện có
            Product product = optionalProduct.get();

            // Cập nhật thông tin sản phẩm
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());

            // Lưu sản phẩm đã cập nhật vào cơ sở dữ liệu
            productRepository.save(product);

            // Ghi log
            log.info("Product {} is updated", product.getId());
        } else {
            // Xử lý khi không tìm thấy sản phẩm với ID đã cho
            log.error("Product with ID {} not found", id);
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }

    public void deleteProduct(String id)
    {
        // Tìm sản phẩm hiện có theo ID
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            // Xóa sản phẩm khỏi cơ sở dữ liệu
            productRepository.deleteById(id);

            // Ghi log
            log.info("Product {} is deleted", id);
        } else {
            // Xử lý khi không tìm thấy sản phẩm với ID đã cho
            log.error("Product with ID {} not found", id);
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    public List<ProductExisting> isExisting(List<String> skuCodes) {
        return skuCodes.stream()
                .map(skuCode -> {
                    Boolean exist = productRepository.existsBySkuCode(skuCode);
                    ProductExisting productExisting;
                    if(exist)
                    {
                        Product product = productRepository.findAllBySkuCode(skuCode);
                        productExisting = ProductExisting.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .skuCode(product.getSkuCode())
                                .isExisting(exist)
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .build();
                    }
                    else
                    {
                        productExisting = ProductExisting.builder()
                                .skuCode(skuCode)
                                .isExisting(exist)
                                .build();
                    }
                    return productExisting;
                })
                .toList();
    }
}
