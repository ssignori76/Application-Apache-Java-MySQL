package com.example.backend.dto;

import com.example.backend.model.AppOrder;
import java.time.LocalDateTime;

public class OrderDto {
    private Long id;
    private Long serviceId;
    private String serviceName;
    private Long userId;
    private String customerName;
    private String status;
    private String note;
    private LocalDateTime createdAt;

    public OrderDto() {}

    public OrderDto(AppOrder order) {
        this.id = order.getId();
        this.serviceId = order.getService().getId();
        this.serviceName = order.getService().getName();
        this.userId = order.getUser().getId();
        this.customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        this.status = order.getStatus();
        this.note = order.getNote();
        this.createdAt = order.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
