
# To-Do List Management API Documentation

This API provides role-based access to manage to-do lists for Employees, Company Admins, and Super Users.




---

## Base URL

```
http://localhost:8080/api
```
---

## Endpoints

### 1. Get All Users

Retrieve a list of all users in the system.

**Endpoint:**
```
GET /api/user
```

**Response:**
```json
[
    {
        "id": 1,
        "name": "John Doe",
        "role": "EMPLOYEE"
    },
    {
        "id": 2,
        "name": "Jane Smith",
        "role": "COMP_ADMIN"
    }
]
```

---

### 2. Get User by ID

Retrieve details of a specific user by their ID.

**Endpoint:**
```
GET /api/user/{id}
```

**Path Parameters:**
| Parameter | Type   | Description           |
|-----------|--------|-----------------------|
| `id`      | Long   | The ID of the user.   |

**Response:**
```json
{
    "id": 1,
    "name": "John Doe",
    "role": "EMPLOYEE"
}
```

---

### 3. Create a New User

Create a new user in the system.

**Endpoint:**
```
POST /api/user
```

**Request Body:**
```json
{
    "name": "New User",
    "role": "EMPLOYEE"
}
```

**Response:**
```json
{
    "id": 101,
    "name": "New User",
    "role": "EMPLOYEE"
}
```

**Response Status:**
- `201 Created`

---

### 4. Delete a User

Delete an existing user by their ID.

**Endpoint:**
```
DELETE /api/user/{id}
```

**Path Parameters:**
| Parameter | Type   | Description           |
|-----------|--------|-----------------------|
| `id`      | Long   | The ID of the user.   |

**Response:**
```json
{
    "id": 101,
    "message": "User successfully deleted"
}
```

---


---

## TODO Endpoints

### 1. Get Todos for a User

Retrieve all tasks for a specific user.

**Endpoint:**
```
GET /todos/{userId}
```

**Headers:**
| Key              | Value             |
|-------------------|-------------------|
| `requestingUserId` | (Your User ID)    |

**Path Parameters:**
| Parameter | Type   | Description                    |
|-----------|--------|--------------------------------|
| `userId`  | Long   | The ID of the target user.     |

**Response:**
```json
[
    {
        "id": 1,
        "description": "Task description",
        "isCompleted": false,
        "user": {
            "id": 1,
            "name": "Employee User",
            "role": "EMPLOYEE"
        }
    }
]
```

---

### 2. Create a Todo

Add a new task for a specific user.

**Endpoint:**
```
POST /todos
```

**Headers:**
| Key              | Value             |
|-------------------|-------------------|
| `requestingUserId` | (Your User ID)    |

**Body:**
```json
{
    "description": "New Task",
    "isCompleted": false,
    "userId": 1
}
```

**Response:**
```json
{
    "id": 101,
    "description": "New Task",
    "isCompleted": false,
    "userId": 1
}
```

---

### 3. Update a Todo

Update an existing task.

**Endpoint:**
```
PUT /todos/{todoId}
```

**Headers:**
| Key              | Value             |
|-------------------|-------------------|
| `requestingUserId` | (Your User ID)    |

**Path Parameters:**
| Parameter | Type   | Description           |
|-----------|--------|-----------------------|
| `todoId`  | Long   | The ID of the to-do.  |

**Body:**
```json
{
    "description": "Updated Task",
    "isCompleted": true,
    "user": {
        "id": 1,
        "name": "Employee User",
        "role": "EMPLOYEE"
    }
}
```

**Response:**
No Content (HTTP 204)

---

### 4. Delete a Todo

Delete an existing task.

**Endpoint:**
```
DELETE /todos/{todoId}
```

**Headers:**
| Key              | Value             |
|-------------------|-------------------|
| `requestingUserId` | (Your User ID)    |

**Path Parameters:**
| Parameter | Type   | Description           |
|-----------|--------|-----------------------|
| `todoId`  | Long   | The ID of the to-do.  |

**Response:**
```json
{
    "id": 101,
    "message": "Todo successfully deleted"
}
```

---
