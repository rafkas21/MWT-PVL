
Shopping List Application

This is a modular shopping list application built using Spring Boot, Thymeleaf, and Docker. It follows a multi-module structure to separate frontend and backend logic.

Features:
- Add, update, delete, and search for shopping items.
- Frontend server-side rendering using Thymeleaf.
- Backend RESTful API with a shared module for common components.
- Dockerized setup for easy deployment and testing.



Backend: 
1. Start backend: DemoApplication.java(\demo\demo\src\main\java\com\example\demo\DemoApplication.java)
2. Open bash (/demo/demo)
    2.1. ./mvnw clean package
    2.2. docker build -t shopping-app:latest .
    2.3. docker-compose up --build

Now the backend should be running in a docker container called demo with a postgres-db and a shopping-app-1 inside. 
The backend and frontend contain some duplicated code which is still included to make the backend work independently (accessable at port 8080). Using e.g. Postman you should be able to send GET/POST/PUT/DEL requests using raw json in the body: 

Get all items: GET http://localhost:8080/api/shoppingItems
Get item by name (has to exist, otherwise the response body is empty): GET http://localhost:8080/api/shoppingItems/{name}
Add item: POST http://localhost:8080/api/shoppingItems
Update item: PUT http://localhost:8080/api/shoppingItems/{name}
Delete item: DELETE http://localhost:8080/api/shoppingItems/{name}


Frontend:
1. Open the frontend in an additional IDE window
2. Start frontend: DemoFrontendApplication.java (\demo-frontend\demoFrontend\src\main\java\com\example\demo\DemoFrontendApplication.java)
3. Open bash (/demo-frontend/demoFrontend)
    3.1. ./mvnw clean package
    3.2. docker-compose build
    3.3. docker-compose up

After this the frontend is accessable with a browser at port 8082 (http://localhost:8082/). 
On top of this simple frontend is the shopping list displayed. You can use the textfield and update button to update the amount. The delete button removes the item. 
You can also add a new item or search for a specific item (by name). 



--- 12-FACTOR-PRINCIPLES ---

1. Codebase: One codebase tracked in revision control, many deploys
Application is divided into a frontend and backend, all under a single git repository. This ensures a single codebase for the entire system. Additionally, git allows version control. 

2. Dependencies: Explicitly declare and isolate dependencies
Dependencies are declared in the pom.xml file (Maven) and managed centrally for each module.

3. Config: Store config in the environment
Configurations such as database URLs and backend API URLs are set in application.properties or environment variables in Docker Compose.

4. Backing services: Treat backing services as attached resources
The application connects to the backend as a service (hosted in a Docker container). The backend API is abstracted as a network service accessible via an environment variable.

5. Build, release, run: Strictly separate build and run stages
The Maven package command builds a JAR, which is then deployed using Docker. Each Docker image is tagged to distinguish between builds.

6. Processes: Execute the app as one or more stateless processes
The frontend and backend are stateless; all state (data) is stored in a database or in-memory cache. Requests are independent.

7. Port binding: Export services via port binding
Each module exposes its service via a specific port (8080 for backend, 8082 for frontend), configured in application.properties and Docker Compose.

8. Concurrency: Scale out via the process model
The app supports scaling via Docker Compose and Kubernetes, where multiple instances of the frontend and backend can run independently.

9. Disposability: Maximize robustness with fast startup and graceful shutdown
Spring Boot applications start quickly, and Docker ensures containers can be stopped and restarted gracefully.

10. Dev/prod parity: Keep development, staging, and production as similar as possible
The Dockerized setup ensures consistency across local development, staging, and production environments.

11. Logs: Treat logs as event streams
Spring Boot logs are output to the console, which Docker captures and aggregates for easy access.

12. Admin processes: Run admin/management tasks as one-off processes
Admin tasks (e.g., database migrations, clearing caches) can be executed via Spring Boot CLI or separate Docker containers.