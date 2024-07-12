package com.programmingtechie.inventory_service.repository;

import com.programmingtechie.inventory_service.model.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, String>
{
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
    Inventory findBySkuCode(String skuCode);

    // Sử dụng Pessimistic Locking để khóa bản ghi trong quá trình giao dịch,
    // ngăn chặn các giao dịch khác thay đổi dữ liệu cho đến khi giao dịch hiện tại hoàn thành.
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT i FROM Inventory i WHERE i.skuCode = :skuCode")
//    Inventory findBySkuCodeWithLock(@Param("skuCode") String skuCode);
}
