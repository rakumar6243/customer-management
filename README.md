
# Customer Management Application

## Overview
This is a simple Customer Management Application built using **Spring Boot**. It leverages an embedded H2 database for quick setup and testing.
Below are detailed steps to run and deploy the application in local, Docker, and Minikube environments, along with instructions for setting up a CI/CD pipeline.

## Software Used

- **Java**: Version 17  
- **Spring Boot**: Version 3.1.4  
- **Database**: H2 Embedded Database  
- **Testing Frameworks**:  
  - JUnit  
  - Mockito  
- **Utility**: Lombok  

**Note**: Ensure you install the respective Lombok plugins for your IDE.

---

## Prerequisites

1. **Java**: Ensure Java 17 is installed and configured.  
2. **Maven**: Install Maven and add it to the system path.  
3. **IDE Setup**: If you are using an IDE such as IntelliJ or Eclipse, install the Lombok plugin for proper code generation support.

---

# Getting Started

## Running the Application Locally

### Clone the Repository
```bash
git clone https://github.com/rakumar6243/customer-management.git
```

### Running the Application
```bash
mvn spring-boot:run
```

### Running Tests
```bash
mvn test
```
### Accessing application locally
```bash
Access the application on the default port 
http://localhost:8080
swagger URL 
http://localhost:8080/swagger-ui/index.html
```

---

## Dockerizing and Running the Application

### 1. Build the Application
Navigate to the working directory and run:
```bash
mvn clean package -DskipTests
```

### 2. Build and Run Docker Image
Execute the following commands:
```bash
docker build -t customermanagement .
docker run -p 8080:8080 customermanagement
```

### 3. Access the Application
The application will be available at:
```
http://localhost:8080
```

---

## Running the Application in Minikube

### 1. Install Required Tools
- Minikube  
- kubectl  

### 2. Configure Minikube for Docker
Navigate to the working directory and run:
```bash
eval $(minikube docker-env)
```

### 3. Deploy Application to Minikube
Apply the deployment configuration:
```bash
kubectl apply -f deploymentservice.yaml
```

### 4. Verify Deployment
Check the status of the pods and services:
```bash
kubectl get pods
kubectl get service
```

### 5. Inspect Deployment
```bash
kubectl describe service customer-management-service
kubectl describe pod
```

Ensure the Pod IP addresses match the Service endpoints to confirm successful deployment.

### 6. Access the Application
Retrieve the Node IP:
```bash
kubectl get nodes -o wide
```
Example output:
```
NAME       STATUS   ROLES           AGE   VERSION   INTERNAL-IP    EXTERNAL-IP   OS-IMAGE             KERNEL-VERSION     CONTAINER-RUNTIME
minikube   Ready    control-plane   24h   v1.31.0   192.168.49.2   <none>        Ubuntu 22.04.4 LTS   6.5.0-15-generic   docker://27.2.0
```

Retrieve the NodePort of the service:
```bash
kubectl get service
```
Example output:
```
NAME                          TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
customer-management-service   NodePort    10.96.16.136   <none>        8080:31973/TCP   23h
kubernetes                    ClusterIP   10.96.0.1      <none>        443/TCP          24h
```

Access the application at:
```
http://<Node-IP>:<NodePort>
```

Example:
```
http://192.168.49.2:31973
```

---

## Summary

- **Dockerized Deployment**: Build and run the application locally using Docker.  
- **Minikube Deployment**: Deploy and manage the application on a Kubernetes cluster with Minikube.

---

## CI/CD Pipeline Stages
     
     Refer Jenkinsfile in the project root folder.


### 1. Build Stage

#### Code Checkout:
- Uses the Git plugin to fetch the latest source code from the main branch.  
- Repository URL: `https://github.com/rakumar6243/customer-management.git`.

#### Maven Build:
- Runs `mvn clean install` to compile the code and package it.

#### SonarQube Analysis:
- Executes `mvn sonar:sonar` to analyze the code for issues such as bugs, vulnerabilities, and code smells.  
- Results are sent to the SonarQube server.

SonarQube quality gates are enforced using the `waitForQualityGate()` function:  
- The pipeline halts if the project does not meet the defined quality gate criteria (e.g., 95% code coverage, no critical vulnerabilities).

#### Docker Build and Push:
- Builds a Docker image using the application's Dockerfile.  
- Tags the image as `customer-management`.  
- Pushes the Docker image to Docker Hub.

### 2. Deploy Stage
- Deploys the Docker image to a Kubernetes cluster.  
- Uses a Kubernetes deployment configuration file (`deploymentservice.yaml`) to apply changes to the cluster.
  
---
## Observability and Monitoring
- **Log Statements**: Added log statements at different log levels (INFO, WARN, ERROR) to capture relevant data during application execution. These logs provide valuable insights into the behavior and state of the application, helping in debugging and troubleshooting.
  
- **Log Analytics**: This log data can be collected by any log analytics tool such as Splunk. It enables effective monitoring in production environments by offering detailed visibility into system behavior, performance, and potential issues.
  
- **Application Performance Monitoring (APM)**: Integration with APM tools like New Relic allows for monitoring the health of the application in real-time. These tools provide deeper visibility into application performance, response times, error rates, and other critical metrics, allowing for proactive maintenance and rapid issue resolution.
  
- **Alerts**: By setting up alerts on thresholds such as error rates, response times, or resource usage, we can proactively monitor the application and receive notifications in real-time. This helps in quickly identifying and addressing performance degradation or potential outages.
---
## Running application using java client
### Overview
CustomerClient is a simple Java command-line application that interacts with a Customer Management REST API. This application allows users to manage customer records by performing the following actions:

- **View all customers**
- **Retrieve a customer by UUID**
- **Create a new customer**
- **Update an existing customer**
- **Delete a customer by UUID**

It uses standard Java libraries for HTTP communication and input handling, making it lightweight and easy to run without additional dependencies

### Usage(package:com.customer.management.client)
### 1. Compile the Application
```
javac CustomerClient.java 
```
### 2. Run the Application
```
java CustomerClient.java 
```
### 3. Follow the On-Screen Menu
```
=== Customer Management ===  
1. View All Customers  
2. View Customer by ID  
3. Add New Customer  
4. Update Customer  
5. Delete Customer  
6. Exit  
Enter your choice:
```

