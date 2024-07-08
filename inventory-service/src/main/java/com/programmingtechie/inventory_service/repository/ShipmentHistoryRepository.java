package com.programmingtechie.inventory_service.repository;

import com.programmingtechie.inventory_service.model.ImportHistory;
import com.programmingtechie.inventory_service.model.ShipmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentHistoryRepository extends JpaRepository<ShipmentHistory, String> {
}
