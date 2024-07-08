package com.programmingtechie.inventory_service.repository;

import com.programmingtechie.inventory_service.model.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, String> {
}
