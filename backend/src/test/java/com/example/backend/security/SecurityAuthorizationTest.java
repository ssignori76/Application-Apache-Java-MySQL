package com.example.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerCannotAccessAdminApi() throws Exception {
        mockMvc.perform(get("/api/v1/admin/orders"))
                .andExpect(status().isForbidden()); // Customer trying to access Admin API
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanAccessAdminApi() throws Exception {
        mockMvc.perform(get("/api/v1/admin/orders"))
                .andExpect(status().isOk());
    }
}
