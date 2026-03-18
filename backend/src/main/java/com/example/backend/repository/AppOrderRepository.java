package com.example.backend.repository;

import com.example.backend.model.AppOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppOrderRepository extends JpaRepository<AppOrder, Long> {
    List<AppOrder> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<AppOrder> findByStatus(String status);
}
