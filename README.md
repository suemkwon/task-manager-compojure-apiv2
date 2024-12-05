# Task Manager API with Compojure API v2 and Swagger UI

This is a simple Task Manager API built using **Compojure API v2** (2.0.0-alpha31) with **Swagger UI** for API documentation and testing. The API supports full CRUD operations on tasks, uses in-memory storage, and includes basic schema validation.

## Features

- **Full CRUD operations**: Create, Read, Update, and Delete tasks.
- **Swagger UI**: Access the interactive Swagger UI for easy exploration of the API.
- **In-memory storage**: Tasks are stored using Clojure atoms (non-persistent).
- **Schema validation**: Ensures that data sent to the API is properly structured.
- **Unique task IDs**: Automatically generated for each task.

## Getting Started

### 1. Set up the project

Clone this repository or create a new project directory with the following structure:

```bash
task-manager/
├── project.clj
└── src/
    └── task_manager/
        └── core.clj
```
### 2. Install Leiningen

```bash
brew install leiningen
```

### 3. Install Project Dependencies

```bash
lein deps
```

### 4. Run the API

```bash
lein run
```

### 5. Access Swagger UI

```bash
http://localhost:3000/swagger-ui
```

## API Endpoints

### POST/tasks

Create a new task.

Request Body
```bash
{
  "title": "Task title",
  "description": "Task description",
  "completed": false
}
```

Example:
``` bash
curl -X POST http://localhost:3000/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries", "description":"Milk, eggs, bread", "completed":false}'
```

### GET/tasks

List all tasks.

Example:
```bash
curl http://localhost:3000/tasks
```
### GET/tasks/:id

Get a specific task by its ID.

Example:
```bash
curl http://localhost:3000/tasks/1
```

### PUT/tasks/:id

Update a specific task by its ID.

Request Body
```bash
{
  "title": "Updated task title",
  "description": "Updated task description",
  "completed": true
}
```

Example: 
```bash
curl -X PUT http://localhost:3000/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries", "description":"Milk, eggs, bread, cheese", "completed":true}'
```

### Delete/tasks/:id

Delete a task by its ID.

Example:
```bash
curl -X DELETE http://localhost:3000/tasks/1
```

## Version

Compojure API: v2 (2.0.0-alpha31)
Clojure: 1.10+