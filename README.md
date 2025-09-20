# Academic Management Service (Spring Boot)

Tech stack:
- Java 17, Spring Boot 3.x
- Spring Data JPA, H2 (dev)
- springdoc-openapi (Swagger)
- Lombok

How to run:
1. mvn clean package
2. mvn spring-boot:run
3. Swagger UI: http://localhost:8080/swagger-ui.html

API summary:
- GET /api/students?page=0&size=20
- POST /api/students
- GET /api/students/{id}
- PUT /api/students/{id}
- DELETE /api/students/{id}

- GET /api/courses
- POST /api/courses
- GET /api/courses/{id}
- PUT /api/courses/{id}
- DELETE /api/courses/{id}

- POST /api/enrollments/student/{studentId}
- POST /api/enrollments/{studentId}/{courseId}/grade 
- GET /api/enrollments/student/{studentId}
- GET /api/enrollments/student/{studentId}/gpa

- POST /api/imports/upload (multipart file)
- GET /api/imports/{id} // status

Notes:
- I have enforced business rules in services: prerequisites, capacity, grade validation, transactional boundaries.
- I have also implemented import endpoints which perform async import and returns job id for polling.
