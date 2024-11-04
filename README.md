# Perso|||et
The best way to manage and organize your contacts effortlessly.

[![photo-2024-10-05-15-52-58.jpg](https://i.postimg.cc/Fz8bqDNJ/photo-2024-10-05-15-52-58.jpg)](https://postimg.cc/Pvb8vbLt)


# Technologies Used
Backend: Java, Spring Boot

Database: MySQL

# Backend API

## Home Page
#### Get a welcome message

```
  GET /
```

## User

### Username/Password
#### Register
```
  POST /register
```
| POST Parameter | Type     |
| :-------- | :------- |
| `User` | `class` |

#### Login
```
  POST /login
```
| POST Parameter | Type     |
| :-------- | :------- |
| `User` | `class` |

### OAuth2

You can use **Google** or **GitHub** for Register/Login

If when you Register you get message:
```
Username has already been used!: {your googleId}
```
Try use [Username/Password](https://github.com/MR-Kotuk/PersoNet?tab=readme-ov-file#usernamepassword) for Register, with:

    1. Another username
    2. Use your googleId as password

After that you will be able to login using **Google** or **GitHub**.

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

#### Delete Persons
```
  DELETE /person/
```
| DELETE Parameter | Type     |
| :-------- | :------- |
| `persons` | `List<Person>` |


### Recycle Bin
#### Search person by keyword
```
  GET /recyclebin/search
```
| Parameter | Type     |
| :-------- | :------- |
| `keyword` | `String` |

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
| `persons` | `List<Person>` |

#### Remove from Recycle Bin
```
  DELETE /recyclebin/
```
| Parameter | Type     |
| :-------- | :------- |
| `persons` | `List<Person>` |

#### Clean Recycle Bin
```
  DELETE /recyclebin/clean
```

## Authors

[MR_Kotuk](https://github.com/MR-Kotuk)