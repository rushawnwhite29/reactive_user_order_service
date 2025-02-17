# Order Info Service

### Author: Rushawn White
### Date: 2025-02-17

## Overview

The Order Info Service is a Spring Boot application that provides order information by interacting with external services. It integrates with the Order Search Service and the Product Info Service to fetch and aggregate order details.

## Project Structure

- `order-info-service`: Main service that provides order information.
- `Reactive_Paradigm_FOR_STUDENTS`: Git submodule containing the Order Search Service and Product Info Service.

## Prerequisites

- Docker
- Docker Compose

## Services

- **MongoDB**: Database service used by the Order Info Service.
- **Order Search Service**: External service to search orders by phone number.
- **Product Info Service**: External service to fetch product information.

## Deployment

### Step 1: Clone the Repository

```sh
git clone git@github.com:rushawnwhite29/gridu_rwhite_reactive_user_order_service.git
cd gridu_rwhite_reactive_user_order_service
```

### Step 2: Initialize and Update Submodules

```sh
git submodule update --init --recursive
```

### Step 3: Build and Run the Services

Use Docker Compose to build and run the services:

```sh
docker-compose up --build
```

This command will:

1. Start MongoDB and import initial data.
2. Build and start the Order Search Service.
3. Build and start the Product Info Service.
4. Build and start the Order Info Service.

### Step 4: Access the Services

- **Order Info Service**: `http://localhost:8080`
- **Order Search Service**: `http://localhost:8081`
- **Product Info Service**: `http://localhost:8082`

## Configuration

The `docker-compose.yml` file contains environment variables for configuring the services:

- `MONGO_DB_NAME`: Name of the MongoDB database.
- `MONGO_ROOT_USERNAME`: MongoDB root username.
- `MONGO_ROOT_PASSWORD`: MongoDB root password.
- `SPRING_DATA_MONGODB_URI`: MongoDB connection URI.
- `ORDER_SEARCH_SERVICE_URL`: URL of the Order Search Service.
- `PRODUCT_INFO_SERVICE_URL`: URL of the Product Info Service.

## Stopping the Services

To stop the services, run:

```sh
docker-compose down
```

This command will stop and remove all the containers.

## Additional Information

- **MongoDB Data Import**: The `mongoimport` service imports initial data from `Reactive_Paradigm_FOR_STUDENTS/mongo/data/users.json`.
- **Service Dependencies**: The `order-info-service` depends on MongoDB and the external services to function correctly.

## Troubleshooting

- Ensure Docker and Docker Compose are installed and running.
- Verify that the submodule `Reactive_Paradigm_FOR_STUDENTS` is correctly initialized and updated.
- Check the logs of each service for any errors using:

```sh
docker-compose logs <service_name>
```

Replace `<service_name>` with the name of the service (e.g., `order-info-service`, `mongo`, etc.).