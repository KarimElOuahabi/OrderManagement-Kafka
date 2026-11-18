# OrderManagement-Kafka

An order management system built to practice microservices with Apache Kafka. Three Spring Boot services (order, payment, notification) never call each other directly. They only communicate through Kafka topics.

## Features

- **Order service:** accepts orders over REST, saves them as `PENDING` and publishes an `order.created` event.
- **Payment service:** reads `order.created`, simulates a payment and publishes `payment.completed`.
- **Order status updates:** the order service reads `payment.completed` and sets the order to `PAID` or `PAYMENT_FAILED`.
- **Notification service:** reads both events and logs a notification.
- **Database per service:** order and payment each own their PostgreSQL database.
- **CI/CD:** Jenkins builds each service, pushes Docker images to Docker Hub and deploys to AWS EC2.

## Tech stack

Java 21 · Spring Boot · Apache Kafka (KRaft) · PostgreSQL · Hexagonal architecture · Gradle · Docker Compose · Jenkins

## Related project

[OrderFlow-AWS-Terraform](https://github.com/KarimElOuahabi/OrderFlow-AWS-Terraform): a similar project built with AWS managed services.
