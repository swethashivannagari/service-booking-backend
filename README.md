# Service Booking Backend

## Overview
A backend system for a service marketplace,
where users book services and providers are auto-assigned based on availability.

## Tech Stack
- Java 17
- Spring Boot
- Spring Security (JWT)
- MongoDB
- Maven

## Roles & Actors
- USER: browses services, creates & manages bookings
- PROVIDER: accepts and completes assigned bookings
- ADMIN: manages services and users

## Booking Flow (End-to-End)
1. User selects service and time slot
2. Backend auto-assigns an available provider
3. Booking created in REQUESTED state
4. Provider accepts → ACCEPTED
5. Provider completes → COMPLETED
6. User or provider can cancel where allowed

## Security Design
- JWT-based authentication
- Role-based authorization
- Ownership checks for user/provider actions
- User identity derived from SecurityContext

## Key Backend Features
- Auto-assign provider (slot-based scheduling)
- Idempotent booking creation
- Booking state rules enforced centrally
- Pagination on listing APIs
- Soft delete for users/services
- Audit logging for critical actions

## API Overview
- Auth: register, login
- Services: create, list (public)
- Provider Profile: create, view
- Bookings: create, accept, cancel, complete, list

## Design Decisions
- Slot-based scheduling to avoid overlap complexity
- Backend-controlled provider assignment
- Centralized booking state transitions
- Client-generated idempotency keys

## Future Enhancements
- Provider broadcast & reassignment
- Variable service duration
- Notifications (email / push)
- Maps & location-based matching
- Redis caching
- Kafka-based async events
