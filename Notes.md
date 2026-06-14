# Notes on the application and changes

# Notes

1. Might be worth abstracting the Gradle versions away to a properties file or TOML file
2. If we would like, we can enhance the Spring Doc documentation with annotations on the REST API methods
3. If we have uses for more detailed Security Auth items like Scope or PII, we can add more Security Auth items and
   annotations
4. I left out most logging; I generally find every team thinks about logging differently
5. We can add REST API Versioning if the team would like. I left that out, mostly as each team thinks about it
   differently
6. Wasn't sure if upgrading to a more recent version of Spring Boot was wanted
7. Wasn't sure if upgrading to a more recent version of Java was wanted
8. Wasn't sure if upgrading to a more recent version of Gradle was wanted
9. Didn't add any Fault Tolerance (Resience4J), wasn't sure if we would like that or not

# Changes

1. Added a Service Layer to allow separation of concerns and better organization of code. I tend to think of REST
   Controller layers as Entry Points into the system, and Services as the main logic that can be used across many
   different Entry Points. Entry Points shouldn't have business logic in them really, unless it's about transforming and
   understanding the business model or validation of that particular Entry Point; for example, REST Controller would
   care about Validating the REST API Data Model and Pageable items.
2. Added a GlobalExceptionHandler to handle exceptions and provide standardized error responses to the
   clients.
3. Added Prometheus to be scraped. Depending on what we would like, we could use OpenTelemetry to instrument the
   application or DataDog
4. Added Dockerfile to be used for containerization and orchestration
5. Added Docker Compose files to be used for local development and Integration Testing
6. Added a CI/CD pipeline
7. Added Liquibase for database migrations and version control and added correct settings to the yaml file 
8. Added Hibernate for database persistence and ORM control and added correct settings to the yaml file 
9. Switched to using Lombok for less boilerplate code 
10. Switched to using UUIDs for Entity IDs. This would allow for better scalability and distributed systems, and less
   potential for ID leakage. 
11. Added a few Indices to the Entities to improve performance; just in case we need them. 
12. Added Timing Metrics to track performance and latency 
13. Added Transactional Annotations to ensure that database transactions are properly managed. 
14. Added OpenAPI documentation to the REST API (Seems like that was something that was wanted)
15. Added an Integration Test Suite to ensure that the application is functioning as expected. Itest Package is for
    Integration Tests (live Spring Boot Application with a Live PostgreSQL Database). test Package is for Unit Tests. I
    created the BaseTest and Framework for it, and then had Claude Code generate the actual Integration and Unit Tests. 
16. Added Caffeine Cache to improve performance for frequently accessed data. We could also add Hibernate L2 Cache to
    improve performance for less frequently accessed data. 
17. Set default Jackson serialization and deserialization configurations. 
18. Set up Spring Security for authentication and authorization. Allowing the index pages and OpenAPI, but forcing
    authentication for all other endpoints. Committed the PEM files for local development and testing; but, should be
    removed in a normal company environment. 
19. Added Hibernate Envers to track changes to the Entities and Auditing them. 
20. Added SecurityAuditorAware to ensure that the correct user is being audited. 
21. Switched to using PostgreSQL for the database, as it seemed like that might be wanted 
22. Added Paging and Sorting to the REST API and the Entities 
23. Correctly associated the Employee Entity with the Organization Entity 
24. Added Mapstruct to map the Entities to the REST API Data Model; so, we don't open our Database model to the outside
    world. 
25. Switched to using Constructor Injection instead of Autowired. 
26. Added Thread Propagation to ensure that the correct user is being audited and that the correct MDC and Thread
    Context are being set.
