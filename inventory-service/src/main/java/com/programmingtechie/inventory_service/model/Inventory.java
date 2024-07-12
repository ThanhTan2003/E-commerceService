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

    @PrePersist
    private void ensureId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
