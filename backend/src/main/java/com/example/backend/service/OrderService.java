package com.example.backend.service;

import com.example.backend.dto.OrderRequest;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.exception.UnauthorizedAccessException;
import com.example.backend.model.AppOrder;
import com.example.backend.model.AppService;
import com.example.backend.model.User;
import com.example.backend.repository.AppOrderRepository;
import com.example.backend.repository.AppServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final AppOrderRepository orderRepository;
    private final AppServiceRepository serviceRepository;

    public OrderService(AppOrderRepository orderRepository, AppServiceRepository serviceRepository) {
        this.orderRepository = orderRepository;
        this.serviceRepository = serviceRepository;
    }

    @Transactional
    public AppOrder createOrder(User user, OrderRequest request) {
        AppService service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
        
        AppOrder order = new AppOrder();
        order.setUser(user);
        order.setService(service);
        order.setNote(request.getNote());
        order.setStatus("SUBMITTED");
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<AppOrder> getCustomerOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    @Transactional(readOnly = true)
    public AppOrder getOrderByIdAndUser(Long id, User user) {
        AppOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new UnauthorizedAccessException("Unauthorized to access this order");
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<AppOrder> getAllOrders(String status) {
        if (status != null && !status.isEmpty()) {
            return orderRepository.findByStatus(status);
        }
        return orderRepository.findAll();
    }
}
