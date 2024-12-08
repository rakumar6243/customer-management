package com.customer.management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name = "Customer", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"phoneNumber", "emailAddress"})
       })
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @NotBlank(message = "First name is required.")
    @Size(max = 50, message = "First name must not exceed 50 characters.")
    @Column(name = "firstName", nullable = false, length = 255)
    private String firstName;

    @Column(name = "middleName", length = 255)
    private String middleName;
   
    @NotBlank(message = "Last name is required.")
    @Size(max = 50, message = "Last name must not exceed 50 characters.")
    @Column(name = "lastName", nullable = false, length = 255)
    private String lastName;
    
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is required.")
    @Column(name = "emailAddress", nullable = false, unique = true, length = 255)
    private String emailAddress;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid and follow E.164 format.")
    @Column(name = "phoneNumber", length = 50)
    private String phoneNumber;

}

