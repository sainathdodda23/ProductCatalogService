# ProductCatalogService
# ProductCatalogService

A RESTful Product Catalog Service built using Java and Spring Boot to manage product information through APIs.

## Overview

ProductCatalogService provides APIs to create, retrieve, update, and delete product details. It follows a layered architecture to separate API handling, business logic, and data access.

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Database:** Configure according to your project (MySQL, PostgreSQL, H2, etc.)
- **Data Access:** Spring Data JPA (if configured)
- **API Style:** REST

## Features

- Create a product
- Retrieve product details by ID
- Retrieve the list of products
- Update product information
- Delete a product
- Input validation and exception handling (if implemented)

## Prerequisites

- Java Development Kit (JDK)
- Maven
- IDE such as IntelliJ IDEA
- Database server, if required by your configuration

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/sainathdodda23/ProductCatalogService.git
cd ProductCatalogService
```

### 2. Configure the application

Update `src/main/resources/application.properties` or `application.yml` with your database connection details, if applicable.

Avoid committing database passwords or other secrets to GitHub.

### 3. Build the project

```bash
mvn clean install
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application will start on the port configured in your Spring Boot settings, typically `8080`.

## API Endpoints

Update the paths and request/response examples below to match your controller implementation.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/products` | Retrieve all products |
| GET | `/products/{id}` | Retrieve a product by ID |
| POST | `/products` | Create a product |
| PUT | `/products/{id}` | Update a product |
| DELETE | `/products/{id}` | Delete a product |

## Example Request

### Create a product

```http
POST /products
Content-Type: application/json
```

Example JSON:

```json
{
  "name": "Laptop",
  "description": "Business laptop",
  "price": 65000.00
}
```

The fields above are illustrative; adjust them to match your actual Product model.

## Project Structure

```text
ProductCatalogService/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

## Testing

Run the automated tests using:

```bash
mvn test
```

## Future Improvements

- Add pagination and sorting
- Add request validation
- Implement centralized exception handling
- Add API documentation using Swagger/OpenAPI
- Add unit and integration tests

## Author

Sainath Dodda

## License

Specify a license if you intend to distribute or reuse this project publicly.
