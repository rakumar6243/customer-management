package com.customer.management.test.service;

import com.customer.management.entity.Customer;
import com.customer.management.exception.ResourceNotFoundException;
import com.customer.management.repository.CustomerRepository;
import com.customer.management.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCustomer = new Customer();
        mockCustomer.setId(UUID.randomUUID());
        mockCustomer.setFirstName("Test");
        mockCustomer.setLastName("Email");
        mockCustomer.setEmailAddress("testemail@test.com");
        mockCustomer.setPhoneNumber("1234567890");
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Test");
        newCustomer.setLastName("Email");
        newCustomer.setEmailAddress("testemail@test.com");
        newCustomer.setPhoneNumber("1234567890");

        Customer result = customerService.createCustomer(newCustomer);

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        assertEquals("Email", result.getLastName());
        assertEquals("testemail@test.com", result.getEmailAddress());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void testCreateCustomer_MissingFields_ThrowsException() {
        Customer invalidCustomer = new Customer();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(invalidCustomer);
        });

        assertEquals("First name and last name are required.", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(mockCustomer));

        List<Customer> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test", result.get(0).getFirstName());
        assertEquals("Email", result.get(0).getLastName());
        assertEquals("testemail@test.com", result.get(0).getEmailAddress());
        assertEquals("1234567890", result.get(0).getPhoneNumber());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(mockCustomer.getId())).thenReturn(Optional.of(mockCustomer));

        Customer result = customerService.getCustomerById(mockCustomer.getId());

        assertNotNull(result);
        assertEquals("Test", result.getFirstName());
        assertEquals("Email", result.getLastName());
        assertEquals("testemail@test.com", result.getEmailAddress());
        assertEquals("1234567890", result.getPhoneNumber());
        verify(customerRepository, times(1)).findById(mockCustomer.getId());
    }

    @Test
    void testGetCustomerById_NotFound_ThrowsException() {
        UUID invalidId = UUID.randomUUID();

        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerById(invalidId);
        });

        assertEquals("Customer not found with ID: " + invalidId, exception.getMessage());
        verify(customerRepository, times(1)).findById(invalidId);
    }

    @Test
    void testUpdateCustomer() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Test1");
        updatedCustomer.setLastName("Email1");
        updatedCustomer.setEmailAddress("testemail@test.com");
        updatedCustomer.setPhoneNumber("1234567890");

        when(customerRepository.findById(mockCustomer.getId())).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(mockCustomer.getId(), updatedCustomer);

        assertNotNull(result);
        assertEquals("Test1", result.getFirstName());
        assertEquals("Email1", result.getLastName());
        verify(customerRepository, times(1)).save(mockCustomer);
    }

    @Test
    void testUpdateCustomer_NotFound_ThrowsException() {
        UUID invalidId = UUID.randomUUID();
        Customer updatedCustomer = new Customer();

        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomer(invalidId, updatedCustomer);
        });

        assertEquals("Customer not found with ID: " + invalidId, exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.findById(mockCustomer.getId())).thenReturn(Optional.of(mockCustomer));
        doNothing().when(customerRepository).delete(mockCustomer);

        customerService.deleteCustomer(mockCustomer.getId());

        verify(customerRepository, times(1)).delete(mockCustomer);
    }

    @Test
    void testDeleteCustomer_NotFound_ThrowsException() {
        UUID invalidId = UUID.randomUUID();

        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.deleteCustomer(invalidId);
        });

        assertEquals("Customer not found with ID: " + invalidId, exception.getMessage());
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}

