package com.customer.management.test.integration;

import com.customer.management.entity.Customer;
import com.customer.management.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll(); 

        mockCustomer = new Customer();
        mockCustomer.setFirstName("test");
        mockCustomer.setLastName("email");
        mockCustomer.setEmailAddress("testemail@test.com");
        mockCustomer.setPhoneNumber("12345");
        customerRepository.save(mockCustomer);
    }

    @Test
    void createCustomer_Success() throws Exception {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("test");
        newCustomer.setLastName("email");
        newCustomer.setEmailAddress("testemail1@test.com");
        newCustomer.setPhoneNumber("123456");

        mockMvc.perform(post("/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCustomer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.lastName").value("email"))
                .andExpect(jsonPath("$.emailAddress").value("testemail1@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("123456"));
    }

    @Test
    void getAllCustomers_Success() throws Exception {
        mockMvc.perform(get("/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("test"))
                .andExpect(jsonPath("$[0].lastName").value("email"))
                .andExpect(jsonPath("$[0].emailAddress").value("testemail@test.com"))
                .andExpect(jsonPath("$[0].phoneNumber").value("12345"));
        
    }

    @Test
    void getCustomerById_Success() throws Exception {
        UUID customerId = mockCustomer.getId();

        mockMvc.perform(get("/v1/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("test"))
                .andExpect(jsonPath("$.lastName").value("email"))
        		.andExpect(jsonPath("$.emailAddress").value("testemail@test.com"))
        		.andExpect(jsonPath("$.phoneNumber").value("12345"));
    }

    @Test
    void updateCustomer_Success() throws Exception {
        UUID customerId = mockCustomer.getId();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("test2");
        updatedCustomer.setLastName("email2");
        updatedCustomer.setEmailAddress("testemail@test.com");
        updatedCustomer.setPhoneNumber("12345");

        mockMvc.perform(put("/v1/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("test2"))
                .andExpect(jsonPath("$.lastName").value("email2"));
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        UUID customerId = mockCustomer.getId();

        mockMvc.perform(delete("/v1/customers/{id}", customerId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        UUID invalidId = UUID.randomUUID();

        mockMvc.perform(get("/v1/customers/{id}", invalidId))
                .andExpect(status().isNotFound());
    }
}
