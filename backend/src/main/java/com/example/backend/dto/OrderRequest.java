package com.example.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderRequest {
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @Size(max = 1000, message = "Note must be at most 1000 characters")
    private String note;

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
