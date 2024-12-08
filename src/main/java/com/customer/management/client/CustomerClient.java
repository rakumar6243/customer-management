package com.customer.management.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

public class CustomerClient {

    private static final String BASE_URL = "http://localhost:8080/v1/customers";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Customer Management ===");
            System.out.println("1. View All Customers");
            System.out.println("2. View Customer by ID");
            System.out.println("3. Add New Customer");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> getAllCustomers();
                    case 2 -> {
                        System.out.print("Enter Customer UUID: ");
                        String id = scanner.nextLine();
                        if (isValidUUID(id)) {
                            getCustomerById(id);
                        } else {
                            System.out.println("Invalid UUID format.");
                        }
                    }
                    case 3 -> createCustomer(scanner);
                    case 4 -> updateCustomer(scanner);
                    case 5 -> {
                        System.out.print("Enter Customer UUID: ");
                        String id = scanner.nextLine();
                        if (isValidUUID(id)) {
                            deleteCustomer(id);
                        } else {
                            System.out.println("Invalid UUID format.");
                        }
                    }
                    case 6 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void getAllCustomers() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }
        }
    }

    private static void getCustomerById(String id) throws Exception {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }
        }
    }

    private static void createCustomer(Scanner scanner) throws Exception {
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Middle Name: ");
        String middleName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email Address: ");
        String emailAddress = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format(
            "{\"firstName\": \"%s\", \"middleName\": \"%s\", \"lastName\": \"%s\", \"emailAddress\": \"%s\", \"phoneNumber\": \"%s\"}",
            firstName, middleName, lastName, emailAddress, phoneNumber
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }
        }
    }

    private static void updateCustomer(Scanner scanner) throws Exception {
        System.out.print("Enter Customer UUID: ");
        String id = scanner.nextLine();
        if (!isValidUUID(id)) {
            System.out.println("Invalid UUID format.");
            return;
        }

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Middle Name: ");
        String middleName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email Address: ");
        String emailAddress = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = String.format(
            "{\"id\": \"%s\", \"firstName\": \"%s\", \"middleName\": \"%s\", \"lastName\": \"%s\", \"emailAddress\": \"%s\", \"phoneNumber\": \"%s\"}",
            id, firstName, middleName, lastName, emailAddress, phoneNumber
        );

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        if (conn.getResponseCode() == 200) {
            System.out.println("Customer updated successfully.");
        } else {
            System.out.println("Failed to update customer. HTTP error code: " + conn.getResponseCode());
        }
    }

    private static void deleteCustomer(String id) throws Exception {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        if (conn.getResponseCode() == 204) {
            System.out.println("Customer deleted successfully.");
        } else {
            System.out.println("Failed to delete customer. HTTP error code: " + conn.getResponseCode());
        }
    }
}
