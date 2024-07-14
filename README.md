# Book Library

Book Library is a comprehensive application designed to simplify the management of library resources, facilitating tasks such as book and reader management, borrowing.

## Overview

Upon startup, the application exposes RESTful endpoints that enable administrators to perform various operations, including saving, updating, retrieving, and deleting information related to books and readers.
Additionally, administrators can facilitate book borrowing for readers.


### Functionality highlights

- The application operates around two main entities:


   1. Reader: represents individuals registered within the system, storing essential details such as name, and a unique identifier.


    2. Book: represents the collection of available books, recording attributes such as title and author.

 - Validation: For instance, when attempting to save, update, or delete a book or reader, the system performs validations to verify the feasibility of the action.
If any discrepancies are found, informative error messages are returned to guide administrators.
When administrator tries to save, update or delete book or reader, validations are performed to check if this action can be done.
If the action is not possible, a meaningful error message is returned.


- Borrowing Process: Borrowing a book involves a validations to ascertain the eligibility of the book.
These validation encompass factor of the book's availability.
If the criteria are met, the book is successfully borrowed; otherwise, detailed error messages elucidate the issues encountered.

## Development overview

The Book Library application follows a structured architecture comprising three layers: controllers, service layer, and DAO layer.
Developed using Java 17, it leverages modern language features like the Stream API. The development process adheres to key coding principles such as Clean Code, Clean Architecture, SOLID, DRY, YAGNI, and KISS.

 ### Technology Stack:
- Programming language - Java 17


- Framework - Spring Boot 3


- Dependency management - Maven


- Database - PostgreSQL

- Testing - JUnit 5, Mockito, AssertJ, and H2 database for comprehensive unit and integration testing. Code coverage >= 85%


- Monitoring -  To Be Implemented

- Documentation - To Be Implemented

- Deployment - To Be Implemented

- CI/CD - To Be Implemented
   
## How to run
// To Be Implemented

## Contact and Support
Feel free to [email](kashtalian1111@gmail.com) me with any questions or suggestions regarding this project.
