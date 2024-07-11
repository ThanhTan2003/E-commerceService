package com.programmingtechie.inventory_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
@Builder
public class Inventory
{
    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 100)
    private String skuCode;

    @Column(nullable = false)
    private Integer quantity;

    @Version
    private Long version; // Thêm trường version để kiểm soát phiên bản

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImportHistory> importHistories = new ArrayList<>();

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShipmentHistory> shipmentHistories = new ArrayList<>();

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public void addImportHistory(ImportHistory importHistory) {
        this.importHistories.add(importHistory);
        importHistory.setInventory(this);
    }

    public void addShipmentHistory(ShipmentHistory shipmentHistory) {
        this.shipmentHistories.add(shipmentHistory);
        shipmentHistory.setInventory(this);
    }
}
