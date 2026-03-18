package com.example.backend.controller;

import com.example.backend.model.AppService;
import com.example.backend.repository.AppServiceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final AppServiceRepository serviceRepository;

    public ServiceController(AppServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    public List<AppService> getServices() {
        return serviceRepository.findAll();
    }
}
