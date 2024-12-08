package com.customer.management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.management.entity.Customer;
import com.customer.management.exception.ResourceNotFoundException;
import com.customer.management.repository.CustomerRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        logger.info("Attempting to create a new customer with details: {}", customer);

        if (customer.getFirstName() == null || customer.getLastName() == null) {
            logger.error("Customer creation failed due to missing required fields: {}", customer);
            throw new IllegalArgumentException("First name and last name are required.");
        }

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return savedCustomer;
    }

    public List<Customer> getAllCustomers() {
        logger.info("Fetching all customers.");
        List<Customer> customers = customerRepository.findAll();
        logger.info("Retrieved {} customers from the database.", customers.size());
        return customers;
    }

    public Customer getCustomerById(UUID id) {
        logger.info("Fetching customer with ID: {}", id);

        return customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with ID: {}", id);
                    return new ResourceNotFoundException("Customer not found with ID: " + id);
                });
    }

    public Customer updateCustomer(UUID id, Customer updatedCustomer) {
        logger.info("Attempting to update customer with ID: {}", id);

        Customer existingCustomer = getCustomerById(id);
        logger.debug("Existing customer details: {}", existingCustomer);

        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setEmailAddress(updatedCustomer.getEmailAddress());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        logger.info("Customer with ID: {} updated successfully.", id);
        return savedCustomer;
    }

    public void deleteCustomer(UUID id) {
        logger.info("Attempting to delete customer with ID: {}", id);

        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);

        logger.info("Customer with ID: {} deleted successfully.", id);
    }
}
