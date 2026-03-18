package com.example.backend.service;

import com.example.backend.dto.OrderRequest;
import com.example.backend.model.AppOrder;
import com.example.backend.model.AppService;
import com.example.backend.model.User;
import com.example.backend.repository.AppOrderRepository;
import com.example.backend.repository.AppServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private AppOrderRepository orderRepository;

    @Mock
    private AppServiceRepository serviceRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        User user = new User();
        user.setId(1L);

        OrderRequest req = new OrderRequest();
        req.setServiceId(1L);
        req.setNote("Test");

        AppService s = new AppService();
        s.setId(1L);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(s));
        when(orderRepository.save(any())).thenReturn(new AppOrder());

        AppOrder res = orderService.createOrder(user, req);
        assertNotNull(res);
    }
}
