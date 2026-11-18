# OrderManagement-Kafka

An order management system built to practice microservices with Apache Kafka. Three Spring Boot services (order, payment, notification) never call each other directly. They only talk through Kafka topics.

## How it works

1. A client sends `POST /orders` to `order-service`. The order is saved as `PENDING` and an `order.created` event is published.
2. `payment-service` reads `order.created`, simulates a payment (about 80% succeed) and publishes `payment.completed`.
3. `order-service` reads `payment.completed` and sets the order to `PAID` or `PAYMENT_FAILED`.
4. `notification-service` reads both events and logs a notification.

Each service has its own PostgreSQL database (except notification, which has none).

## Tech stack

Java 21 · Spring Boot · Apache Kafka (KRaft) · PostgreSQL · Gradle · Docker Compose · Jenkins

Each service uses hexagonal architecture (domain, application, adapters).

## Run it

You need Docker with Docker Compose.

```bash
export CACHEBUST=$(git rev-parse HEAD)
docker compose up --build
```

Place an order:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"price": 49.90}'
```

Then watch the logs of `payment-service` and `notification-service`.

## CI/CD

The `Jenkinsfile` builds each service with Gradle, builds and pushes Docker images to Docker Hub (tagged with the commit hash), then deploys to AWS EC2 over SSH.

## Limitations

- The database passwords are local defaults.
- No authentication on the API.
- Only basic tests.
- No retries or dead-letter topics.

See also [OrderFlow-AWS-Terraform](https://github.com/KarimElOuahabi/OrderFlow-AWS-Terraform), a similar project built with AWS managed services.
