package com.programmingtechie.product_service.repository;

import com.programmingtechie.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
// Danh dau interface ProductRepository la mot REST repository.
// Annotation nay duoc su dung de cau hinh cac endpoint RESTful cho repository,
// cho phep dieu chinh duong dan URL va cac thuoc tinh khac cua API REST.

public interface ProductRepository extends JpaRepository<Product, String> {

}

// JpaRepository<Product, String>: La mot generic interface cua Spring Data JPA.

// ProductRepository se tu dong co cac phuong thuc nhu:

// findAll(): Lay danh sach tat ca san pham.
// findById(String id): Tim san pham theo id.
// save(Product product): Luu hoac cap nhat san pham.
// deleteById(String id): Xoa san pham theo id.