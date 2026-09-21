# Microservices API Postman Test Collection

This directory contains a complete **Postman Collection (v2.1.0)** covering **all API endpoints, test scenarios (positive, negative, validation errors, security, edge cases)** and automatic token handling across the Microservices architecture.

---

## 📁 File Information
- **Collection File:** `postman-collection/Microservices_Postman_Collection.json`
- **Format:** Postman Collection v2.1.0

---

## 🚀 Microservices Architecture & Base URLs

| Service Name | Gateway Route Path | Direct Port | Base URL |
| :--- | :--- | :--- | :--- |
| **API Gateway** | `/` | `9000` | `http://localhost:9000` |
| **Auth Service** | `/api/v1/auth/**` | `8082` | `http://localhost:8082` |
| **Employee Service** | `/api/v1/employee/**` | `8080` | `http://localhost:8080` |
| **Address Service** | `/api/v1/addresses/**` | `8081` | `http://localhost:8081` |
| **Eureka Server** | `/eureka` | `8761` | `http://localhost:8761` |

---

## ⚙️ Postman Collection Variables

The collection includes auto-configured collection variables:

| Variable Name | Default Value | Description |
| :--- | :--- | :--- |
| `baseUrl` | `http://localhost:9000` | Base URL of API Gateway |
| `authServiceUrl` | `http://localhost:8082` | Direct URL of Auth Service |
| `employeeServiceUrl` | `http://localhost:8080` | Direct URL of Employee Service |
| `addressServiceUrl` | `http://localhost:8081` | Direct URL of Address Service |
| `eurekaUrl` | `http://localhost:8761` | Eureka Registry Dashboard URL |
| `jwtToken` | `""` | **Auto-captured** JWT Token after calling Login endpoint |
| `employeeId` | `"1"` | **Auto-captured** Employee ID after calling Create Employee |

> **💡 Automatic JWT Capture:** When you run `1.4 User Login - Success`, a test script automatically extracts the JWT token from the response and saves it in `jwtToken`. Subsequent requests use `Bearer {{jwtToken}}` seamlessly!

---

## 📋 Scenarios Included in the Collection

### 1. Authentication Service (`/api/v1/auth`)
1. **1.1 Register User - Success:** Standard user registration (`ROLE_USER`).
2. **1.2 Register Admin User - Success:** Admin user registration (`ROLE_ADMIN`).
3. **1.3 Register User - Validation Failure:** Negative test verifying field validations (empty fields, invalid email format, short password).
4. **1.4 User Login - Success (Auto-saves JWT):** Authenticates and automatically stores JWT token in Postman environment.
5. **1.5 User Login - Invalid Credentials:** Verifies 401/400 error on wrong password.
6. **1.6 Get Current User (/me) - Authorized:** Retrieves logged-in user profile using `Bearer {{jwtToken}}`.
7. **1.7 Get Current User (/me) - Unauthorized (No Token):** Verifies 401 Unauthorized when Authorization header is missing.
8. **1.8 Validate Token - Via Header:** Validates JWT passed in `Authorization` header.
9. **1.9 Validate Token - Via Query Parameter:** Validates JWT passed via `?token=` parameter.
10. **1.10 Validate Token - Malformed/Invalid Token:** Tests handling of malformed JWT strings.

---

### 2. Employee Service (`/api/v1/employee`)
1. **2.1 Create Employee - Success:** Creates employee and captures generated ID into `{{employeeId}}`.
2. **2.2 Create Employee - Validation Error:** Tests Spring Validation for missing mandatory fields (`empName`, `empEmail`, `empCode`, `companyName`).
3. **2.3 Create Employee - Unauthorized:** Verifies gateway rejects requests without JWT.
4. **2.4 Get Employee By ID - Success (With Feign Address Details):** Fetches employee profile including linked addresses via Feign client integration.
5. **2.5 Get Employee By ID - Not Found:** Verifies 404 response for non-existent employee ID.
6. **2.6 Get All Employees - Default Pagination:** Tests pagination (`page=0`, `size=10`, `sortBy=empName`, `direction=asc`).
7. **2.7 Get All Employees - Custom Pagination & Sorting:** Tests custom page size (`size=5`) and descending sort by `empEmail`.
8. **2.8 Update Employee - Success:** Updates employee details by ID.
9. **2.9 Update Employee - Non-Existent ID:** Verifies 404 error when updating non-existent ID.
10. **2.10 Delete Employee - Success:** Deletes employee record by ID.

---

### 3. Address Service (`/api/v1/addresses`)
1. **3.1 Save Addresses - Success (Multiple):** Saves list of addresses (`HOME`, `OFFICE`) for an employee.
2. **3.2 Save Addresses - Validation Error:** Tests validation rules for negative pin codes, missing street/city, and null address types.
3. **3.3 Get Addresses By Employee ID:** Fetches all address records for an employee.
4. **3.4 Get Address By Type - HOME:** Fetches specific `HOME` address by employee ID and type.
5. **3.5 Get Address By Type - OFFICE:** Fetches specific `OFFICE` address by employee ID and type.
6. **3.6 Get Address By Invalid Type:** Tests 400 Bad Request when passing invalid enum value (`INVALID_TYPE`).
7. **3.7 Update Address By Type - HOME:** Updates details for `HOME` address.
8. **3.8 Delete Address By Type - OFFICE:** Deletes specific address type (`204 No Content`).
9. **3.9 Delete All Addresses By Employee ID:** Deletes all addresses associated with an employee ID (`204 No Content`).

---

### 4. Direct Microservice Calls (Bypassing Gateway)
1. **4.1 Direct Auth Service - Register:** `POST http://localhost:8082/api/v1/auth/register`
2. **4.2 Direct Auth Service - Login:** `POST http://localhost:8082/api/v1/auth/login`
3. **4.3 Direct Employee Service - Get Employee:** `GET http://localhost:8080/api/v1/employee/1`
4. **4.4 Direct Address Service - Get Addresses:** `GET http://localhost:8081/api/v1/addresses/employee/1`

---

### 5. Actuator & Service Discovery
1. **5.1 API Gateway - Health Check:** `GET http://localhost:9000/actuator/health`
2. **5.2 API Gateway - Routes Inspection:** `GET http://localhost:9000/actuator/gateway/routes`
3. **5.3 Eureka Server Dashboard:** `GET http://localhost:8761`

---

## 📥 How to Import in Postman

1. Open **Postman**.
2. Click **Import** (top left).
3. Select or drag-and-drop the JSON file:
   `postman-collection/Microservices_Postman_Collection.json`
4. Run the requests step-by-step or run the whole collection via **Collection Runner**.
