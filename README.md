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
Phase 2 includes:
Redis caching
Redis evict , TTL nd expiration added
Redis cache eviction on URL update

Rate limiter using Bucket4js
Rate limiter configuration

Analytics tracking added
persistence of analytics events currently in PostgreSQL
Asynchronous analytics tracking

Next step : Dockerization and moving to phase 3.

Will update the readme as we progress in this project.
