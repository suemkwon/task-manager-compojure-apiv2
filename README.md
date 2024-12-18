# Task Manager API with Compojure API v2, Feature Flags, and UI

This is a Task Manager API built using **Compojure API v2** (2.0.0-alpha31) with **Swagger UI** for API documentation, feature flags for functionality control, and a web UI for task management. 

## Features

- **Full CRUD operations**: Create, Read, Update, and Delete tasks
- **Feature Flags**: Toggle features like task categories and priorities
- **Web UI**: Modern interface for managing tasks and feature flags
- **Swagger UI**: Interactive API documentation and testing
- **In-memory storage**: Tasks and feature flags stored using Clojure atoms
- **Schema validation**: Ensures proper data structure with dynamic schemas based on enabled features
- **Unique task IDs**: Automatically generated for each task

## Getting Started

### 1. Project Structure
```bash
task-manager/
├── project.clj
├── resources/
│   └── public/
│       └── index.html
└── src/
    └── compojure_apiv2/
        ├── core.clj
        └── feature_flags.clj
```

### 2. Install Dependencies
```bash
lein deps
```

### 3. Run the Application
```bash
lein run
```

### 4. Access the Application
- **Web UI**: http://localhost:3000
- **Swagger UI**: http://localhost:3000/swagger-ui

## API Endpoints

### Tasks

#### POST /api/tasks
Create a new task.

Request Body (with all features enabled):
```json
{
  "title": "Task title",
  "description": "Task description",
  "completed": false,
  "category": "work",
  "priority": "high"
}
```

#### GET /api/tasks
List all tasks.

#### GET /api/tasks/:id
Get a specific task.

#### PUT /api/tasks/:id
Update a task.

#### DELETE /api/tasks/:id
Delete a task.

### Feature Flags

#### GET /api/feature-flags
List all feature flags.

Response example:
```json
[
  {
    "id": "task-categories",
    "name": "Task Categories",
    "description": "Enable task categorization feature",
    "enabled": false
  },
  {
    "id": "task-priorities",
    "name": "Task Priorities",
    "description": "Enable task priority levels",
    "enabled": true
  }
]
```

#### POST /api/feature-flags/:id/toggle
Toggle a feature flag.

## Feature Flags

The application includes two feature flags:

1. **Task Categories** (`task-categories`)
   - When enabled: Allows assigning categories to tasks
   - Affects: Task schema, UI form fields, task display

2. **Task Priorities** (`task-priorities`)
   - When enabled: Allows setting priority levels (low/medium/high)
   - Affects: Task schema, UI form fields, task display

Feature flags can be toggled through:
- The web UI's feature flags section
- The API endpoint: `POST /api/feature-flags/:id/toggle`

## Web UI Features

The modern web interface includes:
- Task management (create, update, delete, toggle completion)
- Feature flag controls
- Responsive design with Tailwind CSS
- Dynamic form fields based on enabled features
- Real-time updates when toggling features

## Technologies Used

- **Backend**
  - Compojure API v2
  - Ring
  - Schema
  - Cheshire

- **Frontend**
  - Alpine.js
  - Axios
  - Tailwind CSS

## Development

To run in development mode:
```bash
lein ring server-headless
```

This will start the server with automatic code reloading.

## Version
0.1.0-SNAPSHOT
