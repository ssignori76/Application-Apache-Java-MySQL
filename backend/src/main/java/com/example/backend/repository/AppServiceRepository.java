package com.example.backend.repository;

import com.example.backend.model.AppService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppServiceRepository extends JpaRepository<AppService, Long> {
}
