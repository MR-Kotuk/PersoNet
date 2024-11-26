# Perso|||et
The best way to manage and organize your contacts effortlessly.

[![photo-2024-10-05-15-52-58.jpg](https://i.postimg.cc/Fz8bqDNJ/photo-2024-10-05-15-52-58.jpg)](https://postimg.cc/Pvb8vbLt)

# Technologies Used
Backend: Java, Spring Boot

Database: MySQL

Other: BCrypt, UUID, lombok

# Backend API

## Home Page
#### Get a welcome message
```
  GET /
```

## User
### Username/Email/Password
#### Register
After registering, you will need to verify your email address using a token **(lifetime - 1 min)**
```
  POST /auth/register
```
| POST Parameter | Type     |
| :-------- | :------- |
| `User` | `class` |

Example JSON
```
{
    "username": "MR_Kotuk",
    "email": "mrkotuk333@gmail.com",
    "password": "kotuk"
}
```

#### Login
If you don't verify your email on registration, you have to do it on login. 
**You can't login if you don't verify email address.**
```
  POST /auth/login
```
| POST Parameter | Type     |
| :-------- | :------- |
| `User` | `class` |

Example JSON
```
{
    "username": "MR_Kotuk",
    "email": "mrkotuk333@gmail.com",
    "password": "kotuk"
}
```

#### Verify Email
```
  GET /auth/verify-email/{token}
```
| Path Parameter | Type |
| :-------- | :------- |
| `token` | `String` |

Example
```
GET /auth/verify-email/3234
```

### OAuth2

You can use **Google** or **GitHub** for Register/Login

## Person

### Analytic
#### Get Person Analytic
```
  GET /person/analytic
```

### Templates
#### Get Templates
```
  GET /person/templates/
```

#### Get Template
```
  GET /person/templates/{personType}
```
| Path Parameter | Type     | Types of PersonType                |
| :-------- | :------- | :------------------------- |
| `personType` | `String` | `general/friend/colleague/family/client/custom` |

### Person
#### Search person by keyword
```
  GET /person/search
```
| Parameter | Type     |
| :-------- | :------- |
| `keyword` | `String` |

Example JSON
```
keyword
```

#### Get list of Person
```
  GET /person/
```

#### Get Person
```
  GET /person/{personId}
```
| Path Parameter | Type     |
| :-------- | :------- |
| `personId` | `int` |

#### Add Person
```
  POST /person/
```
| POST Parameter | Type     |
| :-------- | :------- |
| `Person` | `class` |

#### Update Person
```
  PUT /person/
```
| PUT Parameter | Type     |
| :-------- | :------- |
| `Person` | `class` |

Example JSON
```
{
    "personType": "CUSTOM",
    "lineTemplates": [
        {
            "lineName": "First Name",
            "lineType": "string",
            "lineValue": "MR_Kotuk"
        },
        {
            "lineName": "Last Name",
            "lineType": "string",
            "lineValue": "Meaw"
        }
    ]
}
```

#### Delete Persons
```
  DELETE /person/
```
| DELETE Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example JSON
```
[1, 4, 5, 7]
```

### Recycle Bin
#### Search person by keyword
```
  GET /recyclebin/search
```
| Parameter | Type     |
| :-------- | :------- |
| `keyword` | `String` |

Example JSON
```
keyword
```

#### Get list of Persons in Recycle Bin
```
  GET /recyclebin/
```

#### Return from Recycle Bin
```
  POST /recyclebin/
```
| Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example JSON
```
[1, 4, 5, 7]
```

#### Remove from Recycle Bin
```
  DELETE /recyclebin/
```
| Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example JSON
```
[1, 4, 5, 7]
```

#### Clean Recycle Bin
```
  DELETE /recyclebin/clean
```

## Authors

[MR_Kotuk](https://github.com/MR-Kotuk)