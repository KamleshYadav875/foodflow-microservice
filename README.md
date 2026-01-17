# 🍔 FoodFlow – Event‑Driven Food Delivery Backend

FoodFlow is a **production‑grade, microservices‑based food delivery backend** inspired by real‑world platforms like **Swiggy** and **Zomato**.
It is designed with **clean architecture, SOLID principles, event‑driven communication, fault tolerance, and secure payments**.

This repository focuses on **backend system design and scalability**, not just CRUD APIs.

---

## 🏗️ System Architecture

FoodFlow follows a **true microservices architecture** with:

* API Gateway as a single entry point
* Kafka for asynchronous event streaming
* Database‑per‑service pattern
* Secure service‑to‑service communication

### Architecture Diagram

> 📌 The diagram below represents the current implemented architecture.

![FoodFlow System Architecture](./architecture-diagram.png)

**High‑level flow:**

```
Client → API Gateway → Microservices → Kafka → Retry / DLQ → External Systems
```

---

## 🔐 Security Model

* JWT‑based authentication
* Token validation at **API Gateway**
* User context propagated via headers:

  * `X-USER-ID`
  * `X-ROLES`
* Internal APIs protected (service‑to‑service only)
* Razorpay webhook signature verification

---

## 🧩 Microservices Overview

### 1️⃣ API Gateway

**Responsibilities**

* Central entry point
* JWT validation
* Header enrichment
* Route‑level authentication

**Tech**

* Spring Cloud Gateway (Reactive)

---

### 2️⃣ Identity Service

**Responsibilities**

* User registration & login
* JWT generation
* Role management (USER, RESTAURANT, DELIVERY)
* OAuth2 login support

**Highlights**

* BCrypt password hashing
* Kafka‑based role assignment
* Internal APIs for other services

---

### 3️⃣ Restaurant Service

**Responsibilities**

* Restaurant onboarding
* Menu management
* Availability & profile management
* Public restaurant discovery

**Key Points**

* Owner‑level authorization
* Internal APIs for Order Service
* Menu categorized responses

---

### 4️⃣ Order Service (Core Domain)

**Responsibilities**

* Cart management
* Order lifecycle
* Order state machine
* Delivery partner coordination

**Order State Flow**

```
CREATED → PLACED → ACCEPTED → PREPARING → READY → OUT_FOR_PICKUP → PICKED_UP → DELIVERED
```

**Features**

* Strict state transition validation
* Event‑driven updates via Kafka
* Auto‑cancellation scheduler

---

### 5️⃣ Payment Service

**Responsibilities**

* Payment link creation
* Webhook handling
* Payment state management

**Highlights**

* Razorpay integration
* Webhook signature verification
* Idempotent payment processing
* Kafka events for success / failure

---

### 6️⃣ Delivery Service

**Responsibilities**

* Delivery partner onboarding
* Order assignment
* Delivery status updates

**Design**

* Event‑driven assignment
* Partner availability tracking
* Strategy‑based assignment (city‑level)

---

### 7️⃣ Media Service

**Responsibilities**

* Image uploads
* File storage abstraction

**Current Implementation**

* Local file storage
* Easily extendable to AWS S3 / GCS

---

## 🔄 Event‑Driven Architecture (Kafka)

Kafka is the **backbone** of FoodFlow.

### Core Topics

* `order-created`
* `order-ready`
* `payment-link-created`
* `payment-success`
* `payment-failed`
* `delivery-partner-assigned`

### Dead‑Letter Topics (DLQ)

To ensure reliability, every critical topic has a DLQ:

```
order-created.DLT
payment-success.DLT
payment-failed.DLT
order-ready.DLT
```

Messages are retried automatically and moved to DLQ on repeated failure.

---

## 🧠 Design Patterns Used

* API Gateway Pattern
* Database per Service
* Event‑Driven Architecture
* Saga‑ready workflow
* Strategy Pattern (delivery assignment)
* DTO mapping (ModelMapper)
* Idempotent Kafka consumers

---

## 🧪 Reliability & Fault Tolerance

* Kafka retries with backoff
* Dead‑Letter Queues (DLQ)
* Idempotent payment & order events
* Order auto‑cancel scheduler

---

## 🚀 Tech Stack

| Category   | Technology            |
| ---------- | --------------------- |
| Language   | Java 17               |
| Framework  | Spring Boot           |
| Gateway    | Spring Cloud Gateway  |
| Security   | Spring Security + JWT |
| Messaging  | Apache Kafka          |
| Database   | MySQL / PostgreSQL    |
| Discovery  | Eureka                |
| Payments   | Razorpay              |
| Build Tool | Maven                 |

---

## 📈 Comparison with Swiggy / Zomato

| Feature            | FoodFlow | Swiggy / Zomato |
| ------------------ | -------- | --------------- |
| Microservices      | ✅        | ✅               |
| Event‑Driven       | ✅        | ✅               |
| Secure Payments    | ✅        | ✅               |
| DLQ / Retries      | ✅        | ✅               |
| Saga Orchestration | ⚠️ Ready | ✅               |
| ML‑based Delivery  | ❌        | ✅               |

FoodFlow is **~75% architecturally aligned** with real food‑tech platforms.

---

## 🔮 Future Enhancements

* Saga orchestration (Order + Payment + Delivery)
* Refund service
* Redis caching
* Geo‑based delivery assignment
* Kubernetes deployment
* Observability (Prometheus + Grafana)

---

## 👨‍💻 Author

**Kamlesh Yadav**
Backend Software Engineer (Java / Spring Boot)

---

⭐ If you find this project useful, give it a star!


