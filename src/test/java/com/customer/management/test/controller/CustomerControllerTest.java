package com.customer.management.test.controller;

import com.customer.management.controller.CustomerController;
import com.customer.management.entity.Customer;
import com.customer.management.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private Customer mockCustomer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        mockCustomer = new Customer();
        mockCustomer.setId(UUID.randomUUID());
        mockCustomer.setFirstName("Test");
        mockCustomer.setLastName("Email");
        mockCustomer.setEmailAddress("testEmail@test.com");
        mockCustomer.setPhoneNumber("123-321-1234");
    }

    @Test
    public void testCreateCustomer() {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(mockCustomer);

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Test");
        newCustomer.setLastName("Email");
        newCustomer.setEmailAddress("testEmail@test.com");
        newCustomer.setPhoneNumber("123-321-1234");

        ResponseEntity<Customer> response = customerController.createCustomer(newCustomer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test", response.getBody().getFirstName());
        assertEquals("Email", response.getBody().getLastName());
        assertEquals("testEmail@test.com", response.getBody().getEmailAddress());
        assertEquals("123-321-1234", response.getBody().getPhoneNumber());
        verify(customerService, times(1)).createCustomer(newCustomer);
    }

    @Test
    public void testGetAllCustomers() {
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(mockCustomer));

        ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test", response.getBody().get(0).getFirstName());
        assertEquals("Email", response.getBody().get(0).getLastName());
    }

    @Test
    public void testGetCustomerById() {
        when(customerService.getCustomerById(mockCustomer.getId())).thenReturn(mockCustomer);

        ResponseEntity<Customer> response = customerController.getCustomerById(mockCustomer.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test", response.getBody().getFirstName());
        assertEquals("Email", response.getBody().getLastName());
    }
    
    @Test
    public void testUpdateCustomer() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Test");
        updatedCustomer.setLastName("Email");

        when(customerService.updateCustomer(any(UUID.class), any(Customer.class))).thenReturn(updatedCustomer);

        ResponseEntity<Customer> response = customerController.updateCustomer(mockCustomer.getId(), updatedCustomer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test", response.getBody().getFirstName());
        assertEquals("Email", response.getBody().getLastName());
    }

    @Test
    public void testDeleteCustomer() {
        doNothing().when(customerService).deleteCustomer(mockCustomer.getId());

        ResponseEntity<Void> response = customerController.deleteCustomer(mockCustomer.getId());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(mockCustomer.getId());
    }
}

