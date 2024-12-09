# Perso|||et
The best way to manage and organize your contacts effortlessly.

[![photo-2024-10-05-15-52-58.jpg](https://i.postimg.cc/Fz8bqDNJ/photo-2024-10-05-15-52-58.jpg)](https://postimg.cc/Pvb8vbLt)

## Features
### FlexiMail
A powerful tool for sending personalized messages with ease. Select recipients, use shared details like names and birthdays, and craft one message that’s uniquely tailored for everyone. Perfect for personalized newsletters, greetings, and more - FlexiMail makes every message meaningful!

### Security
Your data is securely encrypted and always protected.

### Simplicity
Five standard templates for quick persona creation.

### Customization
Need more than the standard templates? Create your own unique personas!

### Variety
Separate profiles for personal and work needs.

### Private Personas
Create personas using private templates.

### Public Personas
Create personas using public templates.

### Support
Need help? We're always here to assist!
# Technologies Used
Backend: Java, Spring Boot

Database: MySQL

Other: BCrypt, lombok

# Backend API

## Home Page
#### Get a welcome message
```
  GET /
```

Response:
```
  Welcome {username} to Perso|||et!
```

## FlexiMail
#### Get persons with email
```
  GET /sender/
```

Example of Response:
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Get shared lines
```
  GET /sender/sharedlines
```

| Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example of Request
```
  [1, 2]
```

Example of Response
```
[
    "[</Email/>]",
    "[</First Name/>]",
    "[</Last Name/>]"
]
```

#### Send messages
```
  POST /sender/send
```

| POST Parameter | Type     |
| :-------- | :------- |
| `Message` | `class` |

Example of Request
```
{
  "subject": "Hi [</First Name/>]!",
  "message": "Hi [</First Name/>] [</Last Name/>], this is a test email sent to your email: [</Email/>]. Thanks [</First Name/>] [</Last Name/>], Goodbye!",
  "recipient": [1, 2]
}
```

## Account
#### Get Account Info
```
  GET /account/
```
Example of Response

```
{
    "id": 1,
    "username": "MR_Kotuk",
    "email": "mrkotuk333@gmail.com",
    "password": "**********",
    "verified": true
}
```

#### Set Username
```
  POST /account/set-username
```

Example of Request

(MR_Kotuk -> MR_Kotuk_New)
```
MR_Kotuk_New
```

Response
```
Username changed successful!
```

#### Set Password
```
  POST /account/set-password
```

| POST Parameter | Type     |
| :-------- | :------- |
| `Password` | `class` |

Example of Request
```
{
    "password": "kotuk",
    "newPassword": "kotukkk"
}
```

Response

If password correct:
```
Password changed successful!
```
If password is wrong:
```
Wrong password
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

Example of Request
```
{
    "username": "MR_Kotuk",
    "email": "mrkotuk333@gmail.com",
    "password": "kotuk"
}
```

Response:
```
  Please verify email
```
OR

If your email is already been used
```
  Email has already been used!
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

Example of Request
```
{
    "username": "MR_Kotuk",
    "email": "mrkotuk333@gmail.com",
    "password": "kotuk"
}
```

Response:
```
  {JWT}
```
OR

If you don't verify your email:
```
  Please verify email
```

#### Verify Email
```
  GET /auth/verify-email/{token}
```
| Path Parameter | Type |
| :-------- | :------- |
| `token` | `String` |

Example of Request
```
  GET /auth/verify-email/3234
```

Response:
```
  {JWT}
```
OR

If invalid or expired token
```
  Invalid or expired token
```

### Forgot Password

If you have forgotten your password, you need to follow these steps:
#### 1. Send Verification Email
```
  POST /auth/forgot-password/verify-email/send/{email}
```

| Path Parameter | Type |
| :-------- | :------- |
| `email` | `String` |

Example of Request:
```
  POST /auth/forgot-password/verify-email/send/mrkotuk333@gmail.com
```

Response:
```
Please verify email
```

#### 2. Verify Email
```
  POST /auth/forgot-password/verify-email
```

| Request Body Parameter | Type |
| :-------- | :------- |
| `ForgotPassword` | `class` |

Example of Request
```
{
    "email": "mrkotuk333@gmail.com",
    "verifyEmailToken": 1234,
    "newPassword": "kotuk"
}
```

Response is JWT:
```
  {JWT}
```
OR if Invalid or expired token:
```
  Invalid or expired token
```

### OAuth2

You can use **Google** or **GitHub** for **Register/Login** or **verify** your **email**

## Person

### Analytic
#### Get Person Analytic
```
  GET /person/analytic
```

Response:
```
  {FRIEND=0, COLLEAGUE=0, GENERAL=0, FAMILY=0, CLIENT=0, CUSTOM=0}
```

### Templates
#### Get Templates
```
  GET /person/templates/
```

Response:
```
[
    {
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
    {
        "personType": "FAMILY",
        "lineTemplates": [...]
    },
    {
        "personType": "CUSTOM",
        "lineTemplates": [...]
    },
    {
        "personType": "FRIEND",
        "lineTemplates": [...]
    },
    {
        "personType": "COLLEAGUE",
        "lineTemplates": [...]
    }
]
```

#### Get Template
```
  GET /person/templates/{personType}
```
| Path Parameter | Type     | Types of PersonType                |
| :-------- | :------- | :------------------------- |
| `personType` | `String` | `general/friend/colleague/family/client/custom` |

Response:
```
{
    "personType": {personType},
    "lineTemplates": [...]
}
```

### Person
#### Search person by keyword
```
  GET /person/search
```
| Parameter | Type     |
| :-------- | :------- |
| `keyword` | `String` |

Example of Request
```
{keyword}
```

Example of Response:
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Get list of Person
```
  GET /person/
```

Example of Response:
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Get Person
```
  GET /person/{personId}
```
| Path Parameter | Type     |
| :-------- | :------- |
| `personId` | `int` |

Example of Response:
```
{
    "personId": {personId},
    "email": {user email},
    "creationDate": "...",
    "personStatus": "ACTIVE",
    "personType": "GENERAL",
    "lineTemplates": [...]
}
```

#### Add Person
```
  POST /person/
```
| POST Parameter | Type     |
| :-------- | :------- |
| `Person` | `class` |

Example of Request
```
{
    "personId": ...,
    "email": {user email},
    "creationDate": "...",
    "personStatus": "ACTIVE",
    "personType": "CUSTOM",
    "lineTemplates": [
        {
            "lineName": "First Name",
            "lineValue": "MR_Kotuk"
        },
        {
            "lineName": "Last Name",
            "lineValue": "Meaw"
        }
    ]
}
```

#### Update Person
```
  PUT /person/
```
| PUT Parameter | Type     |
| :-------- | :------- |
| `Person` | `class` |

Example of Request
```
{
    "personId": {personId},
    "email": {user email},
    "creationDate": "...",
    "personStatus": "ACTIVE",
    "personType": "CUSTOM",
    "lineTemplates": [
        {
            "lineName": "First Name",
            "lineValue": "MR_Kotuk"
        },
        {
            "lineName": "Last Name",
            "lineValue": "MeawMeaw"
        }
    ]
}
```

Example of Response:

(List of persons after changes)
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "GENERAL",
        "lineTemplates": []
    },
    {
        "personId": {personId},
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "CUSTOM",
        "lineTemplates": [
            {
                "lineName": "First Name",
                "lineValue": "MR_Kotuk"
            },
            {
                "lineName": "Last Name",
                "lineValue": "MeawMeaw"
            }
        ]
    }
]
```

#### Delete Persons
```
  DELETE /person/
```
| DELETE Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example of Request
```
[1, 4, 5, 7]
```

Example of Response:

(List of persons after changes)
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "ACTIVE",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

### Recycle Bin
#### Search person by keyword
```
  GET /recyclebin/search
```
| Parameter | Type     |
| :-------- | :------- |
| `keyword` | `String` |

Example of Request
```
{keyword}
```

Example of Response:
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Get list of Persons in Recycle Bin
```
  GET /recyclebin/
```

Example of Response:
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Return from Recycle Bin
```
  POST /recyclebin/
```
| Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example of Request
```
[1, 4, 5, 7]
```

Example of Response:

(List of persons in recyclebin after changes)
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Remove from Recycle Bin
```
  DELETE /recyclebin/
```
| Parameter | Type     |
| :-------- | :------- |
| `id` | `List<Integer>` |

Example of Request
```
[1, 4, 5, 7]
```

Example of Response:

(List of persons in recyclebin after changes)
```
[
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "GENERAL",
        "lineTemplates": [...]
    },
    {
        "personId": ...,
        "email": {user email},
        "creationDate": "...",
        "personStatus": "DELETED",
        "personType": "CLIENT",
        "lineTemplates": [...]
    }
]
```

#### Clean Recycle Bin
```
  DELETE /recyclebin/clean
```

## Authors

[MR_Kotuk](https://github.com/MR-Kotuk)