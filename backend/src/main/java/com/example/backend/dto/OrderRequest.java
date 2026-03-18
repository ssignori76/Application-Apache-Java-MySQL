package com.example.backend.dto;

public class OrderRequest {
    private Long serviceId;
    private String note;

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
