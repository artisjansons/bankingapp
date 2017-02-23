# Banking Service

## Overview
This is a simple banking RESTful service built on Spring Boot.

### Redis
This service uses Redis in-memory data structure store to back applications HttpSession
when using REST endpoints.
For the service to work, you must install [Redis 2.8+](https://redis.io/) on localhost and run it with the default port (6379). 
The port is configurable in application properties of this service.

### Gradle Wrapper

The gradle wrapper allows a project to build without having Gradle installed locally. The
executable file will acquire the version of Gradle and other dependencies recommended for
this project.

On Unix-like platforms, such as Linux and Mac OS X:

```
$ ./gradlew bootRun
```

On Windows:

```
$ gradlew bootRun
```

### Testing / Documenting

This project includes Unit tests for controllers, services and repositories.
During the controller tests documentation is created using [Spring REST Docs](https://projects.spring.io/spring-restdocs/) and
asciidoctor.

On Unix-like platforms, such as Linux and Mac OS X:

```
$ ./gradlew test asciidoctor
```

On Windows:

```
$ gradlew test asciidoctor
```

When artifact is deployed you can access the docs under http://localhost:8080/docs/index.html

### Spring Profiles

This application can be run using Spring profiles (dev | production).

The "dev" profile will run with in-memory H2 database and DEBUG logging level.
Hibernate will export schema DDL to the database when the SessionFactory is created. (create-drop)

The "production" profile uses real PostgreSQL database and WARN logging level.
Hibernate will validate the schema. 

```
$ -Dspring.profiles.active=dev|production
```
