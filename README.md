# Lanchonete API

This is a Spring Boot application that provides a RESTful API for a hypothetical Lanchonete (Snack Bar). The application is written in Java and uses SQL for data persistence.

## Features

- Create a new product
- Add a product to an order
- Remove a product from an order
- Get the total price of an order
- Close an order
- Get the total price of a batch of orders
- Get all orders
- Get all products

## Setup

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven: `mvn spring-boot:run`

## API Endpoints

### Order Controller

- `POST /orders/create`: Create a new order
- `POST /orders/add-product`: Add a product to an order
- `POST /orders/remove-product`: Remove a product from an order
- `GET /orders/total/{order_id}`: Get the total price of an order
- `POST /orders/close`: Close an order
- `POST /orders/total_batch`: Get the total price of a batch of products
- `GET /orders/all`: Get all orders

### Product Controller

- `POST /products/create`: Create a new product
- `GET /products/all`: Get all products

## Testing

The project includes unit tests for the service layer. Run the tests with the following command: `mvn test`


## Documentation

https://lanchoneteapi-production.up.railway.app/swagger-ui.html


## ACCESS

https://lanchoneteapi-production.up.railway.app/

## License

[MIT](https://choosealicense.com/licenses/mit/)