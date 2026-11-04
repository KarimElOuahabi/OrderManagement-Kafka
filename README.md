# OrderManagement-Kafka

An event-driven order management system built to practice microservices with Apache Kafka. Three Spring Boot services (`order-service`, `payment-service`, `notification-service`) never call each other directly. They communicate only through Kafka topics: placing an order triggers a payment, and the payment result updates the order and triggers a notification. Each service owns its own PostgreSQL database, and the whole stack runs with one `docker compose up`.

## Architecture

<!--
TODO: add the architecture diagram here (Mermaid flowchart, same style as the OrderFlow README).
Suggested content:
  Client -> order-service (POST /orders) -> order-db
  order-service  --order.created-->      Kafka --> payment-service (-> payment-db), notification-service
  payment-service --payment.completed--> Kafka --> order-service (updates order status), notification-service
-->

> 🚧 **Architecture diagram coming soon.**

**Event flow**

1. A client sends `POST /orders` with a price to `order-service`. The order is saved with status `PENDING` and an `order.created` event is published.
2. `payment-service` consumes `order.created`, simulates the charge (about 80% succeed at random), stores a payment (`COMPLETED` or `FAILED`) and publishes `payment.completed`.
3. `order-service` consumes `payment.completed` and moves the order to `PAID` or `PAYMENT_FAILED`.
4. `notification-service` consumes both `order.created` and `payment.completed` and logs a notification for each.

| Topic | Producer | Consumers (consumer group) |
|---|---|---|
| `order.created` | `order-service` | `payment-service`, `notification-service` |
| `payment.completed` | `payment-service` | `order-service`, `notification-service` |

## Tech stack

| Area | Technology |
|---|---|
| Language and framework | Java 21, Spring Boot 4.1 |
| Architecture | Hexagonal (ports and adapters) in each service |
| Messaging | Apache Kafka 4.1 (KRaft mode, single broker), Spring Kafka, JSON serialization |
| Database | PostgreSQL 16, one database per service, Spring Data JPA |
| Build | Gradle (wrapper included in each service) |
| Containers | Docker, Docker Compose |
| CI/CD | Jenkins, Docker Hub, SSH deploy to EC2 |

## Repository structure

```
OrderManagement-Kafka/
├── Jenkinsfile                  # CI/CD pipeline
├── docker-compose.yml           # Kafka, two Postgres databases and the three services
├── order-service/               # REST API, order domain, Kafka producer and consumer
├── payment-service/             # Payment processing, Kafka consumer and producer
└── notification-service/        # Kafka consumers only, no database
```

Each of `order-service` and `payment-service` follows the same layout under `com.orderms.<service>`:

```
domain/         # entities and status enums, no framework code
application/    # use cases and the ports (interfaces) they depend on
adapters/in/    # REST controller and Kafka listeners
adapters/out/   # JPA repository and Kafka publisher implementing the ports
```

## Docker Compose services

| Service | Image or build | Port | Notes |
|---|---|---|---|
| `kafka` | `apache/kafka:4.1.2` | 9092 | KRaft broker and controller in one node, with a healthcheck |
| `order-db` | `postgres:16` | 5432 | Database `orderdb`, used by `order-service` |
| `payment-db` | `postgres:16` | 5433 | Database `paymentdb`, used by `payment-service` |
| `order-service` | built from `./order-service` | 8080 | Starts after Kafka and `order-db` are healthy |
| `payment-service` | built from `./payment-service` | 8081 | Starts after Kafka and `payment-db` are healthy |
| `notification-service` | built from `./notification-service` | 8082 | Starts after Kafka is healthy |

## Running locally

Requirements: Docker with Docker Compose.

```bash
export CACHEBUST=$(git rev-parse HEAD)   # forces a fresh build of the service images
docker compose up --build
```

Place an order:

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"price": 49.90}'
```

Then watch the logs of `payment-service` and `notification-service` to see the events arrive.

## CI/CD

The `Jenkinsfile` runs these stages:

1. **Checkout** and **Check Java**: fetches the code and prints the Java version.
2. **Build Order, Payment and Notification Service**: `./gradlew clean build` in each service directory.
3. **Build Docker Images**: `docker compose build`, with the git commit hash as the cache-bust argument.
4. **Push Docker Images**: tags each image with the commit hash and pushes it to Docker Hub. Credentials are injected at runtime with Jenkins `withCredentials`.
5. **Deploy to EC2**: connects over SSH with a Jenkins-stored key and runs `docker compose pull` and `up -d` for each service.

## Design decisions

- **Hexagonal architecture:** the domain and use cases depend only on port interfaces. Kafka and JPA live in adapters, so the messaging or persistence technology can change without touching business logic.
- **Choreography instead of orchestration:** no service calls another. Each one reacts to events, which keeps them loosely coupled and independently deployable.
- **Database per service:** `order-service` and `payment-service` each own their data, and no service reads another service's tables.
- **Separate consumer groups:** each service has its own group ID, so `order.created` and `payment.completed` are delivered to every interested service.
- **Typed JSON events with an explicit default type:** consumers deserialize to their own local event class and ignore type headers, so services do not share classes or packages.
- **Healthcheck-gated startup:** services wait for Kafka and their database to report healthy before starting.

## Known limitations and next steps

- **Dual write:** an order is saved and its event published as two separate steps. The transactional outbox pattern, as used in [OrderFlow](https://github.com/KarimElOuahabi/orderflow), would make this atomic.
- **Credentials:** the database passwords are defaults for local development. They should move to environment variables or a secrets manager before any real deployment.
- **Schema management:** Hibernate `ddl-auto: update` creates the tables. Flyway migrations would be safer.
- **Resilience:** no dead-letter topics, retries with backoff or idempotent consumers yet.
- **Tests:** only the default context-load tests exist. Unit tests for the use cases and Testcontainers integration tests are the next step.
- **Notifications:** `notification-service` currently only logs the events. It does not send real emails or messages.
- **Security and scale:** the API has no authentication, and Kafka runs as a single broker without TLS.

## Related project

[OrderFlow](https://github.com/KarimElOuahabi/orderflow) solves a similar problem with AWS managed services (EventBridge, SQS, Lambda, ECS Fargate) and Terraform. This project focuses on Kafka and Docker.
