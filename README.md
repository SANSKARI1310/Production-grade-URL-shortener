Production-grade-URL-shortener
A production-oriented URL shortening and analytics platform built with Java and Spring Boot, progressively evolving toward a distributed, cache-optimized, event-driven backend. The project will be divided in certain phases corresponding to different tech stacks that i will add eventually which will follow my backend-codex repo. 
Phase 1 --> Core Domain, Storage & ID Strategy

PostgreSQL database schema
Flyway database migrations
Domain model
Distributed ID generation
Base62 encoding
URL validation and normalization
HTTP redirect handling
Standardized API error handling
Integration testing with Testcontainers

Phase 1 is over and moving on to phase 2.
Phase 2 --> 
Redis Cache-Aside Architecture
Strict Cache Invalidation (Eviction-on-Write)
JPA Entity Optimization
Setup Foundation for async event management
Containerized Build Pipeline

Phase 2 is over and moving on to phase 3.

phase 3 includes:
more about async mapping
introducing kafka
asynchronous processing + event-driven architecture + decoupling


Will update the readme as we progress in this project.
