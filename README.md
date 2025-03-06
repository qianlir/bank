# Bank Transaction Management System

A modern banking transaction management application built with Java 21 and Spring Boot. This system provides a robust API for managing financial transactions with a focus on performance, reliability, and scalability.

## Key Features

- **Transaction Management**: Create, read, update, and delete transactions
- **Real-time Processing**: In-memory transaction processing for high performance
- **RESTful API**: Clean and well-documented API endpoints
- **Comprehensive Testing**: Unit tests, integration tests, and stress tests
- **Containerization**: Docker and Kubernetes support
- **Caching**: Efficient caching mechanisms for improved performance
- **Validation**: Strong input validation and error handling
- **Pagination**: Efficient data retrieval with pagination support

## Technology Stack

- **Backend**: Java 21, Spring Boot 3.x
- **Frontend**: React.js with Material-UI
- **Database**: In-memory storage (no persistence)
- **Build Tool**: Maven
- **Containerization**: Docker, Kubernetes
- **Testing**: JUnit 5, Mockito, JMeter
- **Caching**: Spring Cache with Caffeine

## API Documentation

### Transactions API

#### Create Transaction
`POST /api/transactions`

**Request Body:**
```json
{
  "type": "TRANSFER|DEPOSIT|WITHDRAWAL",
  "amount": 100.0,
  "description": "string",
  "fromAccountNumber": "string", // Required for TRANSFER and WITHDRAWAL
  "toAccountNumber": "string" // Required for TRANSFER and DEPOSIT
}
```

#### Get All Transactions
`GET /api/transactions?page=0&size=10`

#### Get Transaction by ID
`GET /api/transactions/{id}`

#### Update Transaction
`PUT /api/transactions/{id}`

#### Delete Transaction
`DELETE /api/transactions/{id}`

## Setup Instructions

1. **Prerequisites**:
   - Java 21 JDK
   - Maven 3.8+
   - Node.js 18+ (for frontend)
   - Docker (optional)

2. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/bank-transaction-system.git
   cd bank-transaction-system
   ```

3. **Build the application**:
   ```bash
   mvn clean install
   cd frontend
   npm install
   ```

## Running the Application

### Development Mode
```bash
# Start backend
mvn spring-boot:run

# Start frontend
cd frontend
npm start
```

### Production Mode with Docker
```bash
docker-compose up --build
```

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Stress Testing
```bash
# Using JMeter
jmeter -n -t src/test/jmeter/TransactionLoadTest.jmx
```

## Containerization

The application is fully containerized with Docker:

- **Backend**: Java 21 with Spring Boot
- **Frontend**: Nginx with React build
- **Database**: In-memory (no external database required)

### Kubernetes Deployment
```bash
kubectl apply -f k8s/
```

## Caching Implementation

The system uses Spring Cache with Caffeine for:

- Frequently accessed transaction data
- Account information
- API response caching

Cache configuration can be adjusted in `application.properties`:
```properties
spring.cache.caffeine.spec=maximumSize=500,expireAfterAccess=10m
```

## Performance Considerations

- In-memory storage for fast data access
- Efficient pagination implementation
- Asynchronous processing for high-volume transactions
- Connection pooling for API requests
- Garbage collection optimization for Java 21

## Error Handling

The system provides comprehensive error handling with:

- Custom error responses
- HTTP status codes
- Detailed error messages
- Validation constraints
- Global exception handling

Example error response:
```json
{
  "timestamp": "2025-03-06T14:52:47.123+08:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid transaction amount",
  "path": "/api/transactions"
}
```

## Pagination

The API supports pagination with the following parameters:

- `page`: Page number (0-based)
- `size`: Number of items per page

Example response:
```json
{
  "content": [
    // transaction objects
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 5,
  "totalElements": 50,
  // ... other pagination metadata
}
```

## Future Improvements

- Add transaction categorization
- Implement transaction history export
- Add support for recurring transactions
- Implement transaction approval workflow
- Add support for multiple currencies
- Implement transaction notifications
- Add support for transaction templates
- Implement transaction search and filtering
- Add support for transaction attachments
- Implement transaction reporting

## License

xxxxx
