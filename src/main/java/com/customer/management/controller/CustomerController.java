package com.customer.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.customer.management.entity.Customer;
import com.customer.management.exception.ErrorResponse;
import com.customer.management.service.CustomerService;
import com.customer.management.validation.ValidUUID;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/customers")
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create a new customer")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
@ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
    	logger.debug("Request received: {}", customer);
        logger.info("Received request to create a customer: firstName={}, lastName={}", 
                    customer.getFirstName(), customer.getLastName());
        Customer createdCustomer = customerService.createCustomer(customer);
        logger.info("Customer created successfully with ID={}", createdCustomer.getId());
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @Operation(summary = "Retrieve all customers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        logger.info("Received request to retrieve all customers.");
        List<Customer> customers = customerService.getAllCustomers();
        logger.info("Retrieved {} customers.", customers.size());
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Retrieve a customer by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
@ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable  @ValidUUID(message = "Invalid UUID format.") UUID id) {
        logger.info("Received request to retrieve customer with ID={}", id);
        Customer customer = customerService.getCustomerById(id);
        logger.info("Customer with ID={} retrieved successfully.", id);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Update a customer by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid customer data provided",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
@ApiResponse(responseCode = "404", description = "Customer not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
@ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable @ValidUUID(message = "Invalid UUID format.") UUID id, @Valid @RequestBody Customer updatedCustomer) {
        logger.info("Received request to update customer with ID={}. Update details: firstName={}, lastName={}", 
                    id, updatedCustomer.getFirstName(), updatedCustomer.getLastName());
        Customer updated = customerService.updateCustomer(id, updatedCustomer);
        logger.info("Customer with ID={} updated successfully.", id);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a customer by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
@ApiResponse(responseCode = "500", description = "Internal server error",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @ValidUUID(message = "Invalid UUID format.") UUID id) {
        logger.info("Received request to delete customer with ID={}", id);
        customerService.deleteCustomer(id);
        logger.info("Customer with ID={} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }
}


