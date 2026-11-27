# ProductCatalogService

A RESTful Product Catalog microservice built using **Java 17, Spring Boot, Spring Data JPA, Hibernate and MySQL**, with JWT-based integration to UserAuthService and AI-powered product pitch generation using the Groq API.

## Overview

ProductCatalogService is part of the ShopNest application. It manages product and category information, provides product search and retrieval APIs, and generates AI-based marketing pitches for products.

The service uses MySQL for persistent product storage and communicates with UserAuthService to validate JWTs for protected write operations.

It also integrates with the Groq API to generate short, product-specific promotional messages.

## Features

- Create and retrieve products.
- Retrieve products by ID.
- Store and associate products with categories.
- Retrieve average price for a category.
- Search products by price range.
- Pagination support using Spring Data `Pageable` and `PagedModel`.
- Soft deletion of products using an active/inactive state.
- Import sample products from the external supplier service implemented in the project.
- JWT validation through UserAuthService for non-GET requests.
- Admin-role check for product deletion.
- AI-powered product pitch generation using Groq.
- AWS RDS MySQL persistence.
- Deployment on AWS Elastic Beanstalk.

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot |
| REST APIs | Spring MVC |
| Persistence | Spring Data JPA |
| ORM | Hibernate |
| Database | MySQL |
| Build tool | Maven |
| API documentation dependency | Springdoc OpenAPI |
| HTTP client | Spring RestClient / RestTemplate |
| Authentication integration | JWT |
| AI integration | Groq API |
| Cloud deployment | AWS Elastic Beanstalk |
| Database hosting | AWS RDS MySQL |
| Logging and monitoring | Amazon CloudWatch Logs |

## Architecture

```text
                 Client / Postman
                        |
                        v
               ProductCatalogService
                        |
          +-------------+-------------+
          |             |             |
          v             v             v
      Controller     Security      PitchService
          |           Filter           |
          v             |              v
      Product          v           Groq API
      Service     UserAuthService
          |
          v
     ProductMapper
          |
          v
    Repositories
          |
          v
      MySQL RDS
```

## Project Structure

```text
ProductCatalogService
|
+-- controller
|   +-- ProductController.java
|   +-- CategoryController.java
|   +-- InternalProductController.java
|
+-- dto
|   +-- ProductDto.java
|   +-- CategoryDto.java
|   +-- ProductPitchDto.java
|   +-- AuthUserDto.java
|
+-- mappers
|   +-- ProductMapper.java
|
+-- model
|   +-- Product.java
|   +-- Category.java
|   +-- BaseEntity.java
|
+-- repository
|   +-- ProductRepository.java
|   +-- CategoryRepository.java
|
+-- security
|   +-- TokenValidationFilter.java
|
+-- service
|   +-- StorageProductService.java
|   +-- DummyProductService.java
|   +-- ProductImportService.java
|   +-- CategoryService.java
|   +-- PitchService.java
|
+-- resources
|   +-- application.properties
|   +-- application-prod.properties
|
+-- pom.xml
```

## Main Components

### 1. ProductController

Exposes REST endpoints for:

- Creating products.
- Retrieving all products.
- Retrieving a product by ID.
- Searching products by price range.
- Generating AI product pitches.
- Deleting products.

### 2. StorageProductService

Implements product persistence using Spring Data JPA repositories.

Responsibilities:

- Save products in MySQL.
- Retrieve active products.
- Retrieve products by ID.
- Search products by price range.
- Find or create associated categories.
- Perform soft deletion by changing product state to `INACTIVE`.

### 3. ProductMapper

Converts between Product entities and ProductDto objects.

This separates persistence models from API request and response objects.

### 4. CategoryService

Provides category-related business functionality, including calculating the average product price for a category.

### 5. TokenValidationFilter

Integrates ProductCatalogService with UserAuthService.

For non-GET requests:

1. Reads the Authorization header.
2. Checks for the Bearer token.
3. Calls UserAuthService at `/auth/validateToken`.
4. Passes the token to the authentication service.
5. Rejects invalid tokens with `401 Unauthorized`.
6. Returns `503 Service Unavailable` if the authentication service cannot be reached.
7. Checks for the `ADMIN` role on DELETE requests.

**Current implementation detail:** GET requests bypass this custom token validation filter. POST requests require a valid JWT, and DELETE requests additionally require the ADMIN role.

### 6. PitchService — Groq AI Integration

PitchService integrates with the Groq Chat Completions API.

API endpoint:

```text
https://api.groq.com/openai/v1/chat/completions
```

Configured model:

```text
openai/gpt-oss-20b
```

The service sends product information to Groq, including:

- Product name
- Category
- Price
- Description

The AI is instructed to generate one short promotional push notification with a maximum of 25 words, mention the price, use at most one emoji, and avoid hashtags.

If the Groq request fails, the current implementation returns a simple fallback promotional message.

## REST API Endpoints

### Product APIs

Base path: `/products`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/products` | Retrieve all products |
| GET | `/products/{id}` | Retrieve product by ID |
| POST | `/products` | Create a product |
| DELETE | `/products/{id}` | Soft-delete a product |
| GET | `/products/search` | Search products by price range |
| GET | `/products/{id}/pitch` | Generate an AI product pitch |

### Category API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/categories/{category}` | Retrieve average product price for a category |

### Internal API

| Method | Endpoint | Description |
|---|---|---|
| POST | `/internal/products/import` | Import products from the supplier service |

## API Request Examples

### 1. Create Product

```http
POST /products
Content-Type: application/json
Authorization: Bearer <JWT_TOKEN>
```

Request body:

```json
{
  "name": "Samsung Galaxy S25",
  "description": "Samsung smartphone with advanced camera and AI features",
  "price": 79999.00,
  "imageUrl": "https://example.com/samsung-s25.jpg",
  "category": {
    "name": "Electronics",
    "description": "Electronic devices and accessories"
  }
}
```

A successful request returns `201 Created`.

The product ID is generated by the database.

### 2. Get All Products

```http
GET /products
```

Returns the list of stored products.

### 3. Get Product by ID

```http
GET /products/1
```

Returns the product details for the supplied ID.

### 4. Search Products by Price Range

```http
GET /products/search?minPrice=1000&maxPrice=50000&page=0&size=10
```

Uses Spring Data `Pageable` and returns a `PagedModel` response.

### 5. Generate AI Product Pitch

```http
GET /products/1/pitch
```

Returns an AI-generated promotional pitch for the selected product.

### 6. Get Average Price by Category

```http
GET /categories/Electronics
```

Returns the average product price for the specified category.

### 7. Delete Product

```http
DELETE /products/1
Authorization: Bearer <ADMIN_JWT_TOKEN>
```

The service performs a soft delete by changing the product state to `INACTIVE`.

### 8. Import Products

```http
POST /internal/products/import
Authorization: Bearer <JWT_TOKEN>
```

Imports products from the supplier implementation into the local product database.

## Authentication Flow Between Microservices

ProductCatalogService delegates token validation to UserAuthService instead of independently validating JWT signatures.

```text
Client
  |
  | POST /products
  | Authorization: Bearer <JWT>
  |
  v
ProductCatalogService
  |
  v
TokenValidationFilter
  |
  | GET /auth/validateToken
  | Authorization: Bearer <JWT>
  |
  v
UserAuthService
  |
  | Validates JWT and returns user claims
  |
  v
ProductCatalogService
  |
  | Continues request if authorized
  v
StorageProductService
  |
  v
MySQL Database
```

The current filter allows GET requests to pass without calling UserAuthService. Non-GET requests require a valid Bearer token, and DELETE additionally checks the ADMIN role.

## Database Design

The service uses MySQL and Hibernate/JPA for persistence.

Main entities:

### Product

Stores product information such as:

- ID
- Name
- Description
- Price
- Image URL
- Category association
- State
- Creation and update timestamps

### Category

Stores category information and is associated with products.

The service reuses an existing category by name when creating a product, or creates a new category when one does not already exist.

### Soft Delete

Products are not physically removed when deleted.

Instead, the product state is changed to `INACTIVE`. Active product retrieval uses the product state to exclude inactive records.

## Local Setup

### Prerequisites

- JDK 17
- Maven
- MySQL Server
- IntelliJ IDEA
- Postman
- Groq API key

### 1. Clone the repository

```bash
git clone https://github.com/sainathdodda23/ProductCatalogService.git
cd ProductCatalogService
```

### 2. Create local database

```sql
CREATE DATABASE ecommerce_staging;
```

The local `application.properties` currently uses this database name.

### 3. Configure environment variables

Set the following environment variables:

```text
DB_PASSWORD=<your-local-mysql-password>
GROQ_API_KEY=<your-groq-api-key>
AUTH_SERVICE_URL=http://localhost:8081
```

The local database username is currently configured as `root`.

### 4. Build the project

```bash
mvn clean package
```

### 5. Run the application

```bash
mvn spring-boot:run
```

The local application is configured to run on port `8080`.

Ensure UserAuthService is running on port `8081` when testing authenticated write operations.

## Production Deployment — AWS

ProductCatalogService is deployed on **AWS Elastic Beanstalk** using the Java Corretto 17 platform.

Infrastructure:

- AWS Elastic Beanstalk – application hosting.
- Amazon EC2 – compute instance.
- Amazon RDS MySQL – product and category persistence.
- AWS IAM – application and service permissions.
- Amazon CloudWatch Logs – application log monitoring.
- Groq API – AI product pitch generation.
- UserAuthService – JWT validation.

Production environment:

```text
SPRING_PROFILES_ACTIVE=prod
```

Production environment variables:

| Variable | Purpose |
|---|---|
| `SERVER_PORT` | Application port |
| `DB_URL` | AWS RDS JDBC connection URL |
| `DB_USERNAME` | RDS application username |
| `DB_PASSWORD` | RDS application password |
| `AUTH_SERVICE_URL` | Base URL of deployed UserAuthService |
| `GROQ_API_KEY` | Groq API key |

Production database:

```text
productcatalogservice
```

The production configuration is maintained in:

```text
src/main/resources/application-prod.properties
```

All credentials and API keys must be configured through environment variables, never committed to GitHub.

## Testing

Use Postman to test the following sequence:

1. Retrieve all products.
2. Create a product with a valid JWT.
3. Retrieve the newly created product by ID.
4. Search products by price range.
5. Retrieve category average price.
6. Generate an AI product pitch.
7. Attempt product creation without a token and verify authentication rejection.
8. Attempt product deletion with a non-admin token and verify authorization rejection.
9. Delete a product with an admin token.

## Future Improvements

- Add PUT endpoint for updating products.
- Add more comprehensive request validation.
- Improve centralized exception handling.
- Add unit and integration tests for service and repository layers.
- Improve authentication context propagation.
- Add caching for frequently accessed products.
- Improve logging and observability.
- Introduce API rate limiting.
- Add HTTPS and production security hardening.

## Author

Sainath Dodda

GitHub: https://github.com/sainathdodda23

## License

See the repository LICENSE file.
