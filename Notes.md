# Notes on the application and changes

# Notes

Autowired change to constructor injection
Controller is the entry point and needs to have a Service Layer
Needs GlobalExceptionHandler
Needs Metrics and Timing and others
Need Thread Propagation
Needs to change the yaml files
Needs to change the logging formatter
Needs to add security
Needs to default the JPA items
Needs to switch to Virtual Threads
Needs to add default Jackson stuff
Needs to add an Itest Framework and Integration Tests
Needs to add a Test Database
Entity IDs could be UUIDs to not have sequential IDs
Could switch to using Lombok for less boilerplate code
Could add Caching (like Caffine)
Should we have indices on the Entity Columns, like first name?
If we are using Hibernate, than we need to add the JARs and have better control over the Annotations
Add Liquibase and files
Add a Docker file
Add Docker Compose files
Add a CI/CD pipeline
Probably need transactional annotations
Probably need API versioning
probably need to combine the Repositories for sharing
They seem to want OpenAPI, so let's add it
Abstract the gradle versions away to a properties file or TOML file


Add some talking points about allowing the UI to be correctly established and not just an Index file


# Changes

