# Recursive Test Resource Manager

A JUnit 6 extension for managing test resources with recursive initialization.

## Features

- Declare resource dependencies using the `@UsesResource` annotation
- Resources are identified by interfaces, with dependencies modeled through inheritance
- Automatic recursive initialization of resource dependencies
- Support for resource reinitialization when dirtied by tests
- Parallel test execution support with proper resource locking
- Provide custom initialization logic via `ResourceInitializer` implementations

## Usage

Annotate test methods or classes with `@UsesResource` to declare required resources. Implement `ResourceInitializer` to provide initialization logic for resources.

## Requirements

- Java 17
- JUnit Jupiter 6.1.3

## Building

```bash
mvn clean install
```
