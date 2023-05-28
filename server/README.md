# ticketServiceApp

## Installation guide
### Build the docker image using Jib

The solution is supposed to be run in a linux host.

Make sure you are in the server folder.
Run the following command:
```shell
gradle jibDockerBuild --image=gcr.io/ticketserviceapp/ticketservice && docker-compose up -d

gradlew.bat jibDockerBuild --image=gcr.io/ticketserviceapp/ticketservice && docker-compose -p observabilty up -d
```
If you don't have installed gradle you can use the `gradlew` or `gradlew.bat` commands for linux or windows respectively.

If you are on a windows host, you must delete the line 28 in the [docker-compose.yml](../docker-compose.yml) file.

Then insert the ip address of your host in the [application.properties](./src/main/resources/application.properties) in the first line instead of localhost

Example: _spring.datasource.url=jdbc:postgresql://<your_ip>:5432/postgres_

_**NB**: 'localhost' won't work because when you run the application from a container, the localhost address in the container refers to the container itself, not your local machine.

## List of APIs
- [Authentication APIs](#auth-apis)
  - [login](#login)
  - [signup](#sign-up)
  - [logout](#logout)
- [Products APIs](#products-apis)
  - [Retrieve all products](#retrieve-all-products)
  - [Retrieve-single-product](#retrieve-single-product)
- [Profile APIs](#profiles-apis)
  - [Retrieve profile by email](#retrieve-profile-by-email)
  - [Modify profile](#modify-profile)
- [Ticket APIs](#tickets-apis)
  - [Retrieve ticket by id](#retrieve-ticket-by-id)
  - [Retrieve open tickets](#retrieve-open-tickets)
  - [Retrieve tickets created by profile](#retrieve-tickets-created-by-profile)
  - [Retrieve tickets assigned to profile](#retrieve-tickets-assigned-to-profile)
  - [Create ticket](#create-ticket)
  - [Modify ticket priority](#modify-ticket-priority)
  - [Assign expert to ticket](#assign-expert-to-ticket)
  - [Stop ticket](#stop-ticket)
  - [Close ticket](#close-ticket)
  - [Resolve ticket](#resolve-ticket)
  - [Reopen ticket](#reopen-ticket)

---------------------------------


## Auth APIs

### __Login__

POST `/public/login`

Description: login

Example Request URL: `http://localhost:port/public/login`
Request body:
```
{
    "username": "user1@mail.com",
    "password": "password"
}
```

Successfull Response Header: `200 OK` (success)
Response body:
```
{
    "id": 1,
    "email": "user1@mail.com",
    "username": "user2",
    "name": "MyName",
    "surname": "MySurname",
    "dateOfBirth": "1998-04-23",
    "password": "",
    "token": "tokenLongString"
}
```

Error Response Header:
- `500 Internal Server Error` (generic error)
- `401 Unauthorized` (Invalid credentials)



### __Sign Up__

POST `/public/signup`

Description: create a client user

Example Request URL: `http://localhost:port/public/signup`
Request body:
```
{
    "email": "user1@mail.com",
    "username": "user1",
    "name": "MyName",
    "surname": "MySurname",
    "password": "password",
    "dateOfBirth": "1998-04-23"
}
```

Successfull Response Header: `201 Created` (success)
Response body: 
```
{
    "id": 1,
    "email": "user2@mail.com",
    "username": "user2",
    "name": "Pippo",
    "surname": "Baudo",
    "dateOfBirth": "1998-04-23",
    "password": "",
    "token": null
}
```

Error Response Header:
- `409 Conflicts` (profile with same email already exists)
- `500 Internal Server Error` (generic error)


### __Logout__

DELETE `/authenticated/logout`

Description: delete the session

Example Request URL: `http://localhost:port/authenticated/logout`
Request body:_None_

Successfull Response Header: `200 Ok` (success)
Response body: _None_

Error Response Header:
- `500 Internal Server Error` (generic error)


## Products APIs

### __Retrieve all products__

GET `/public/products`

Description: Get all the products

Example Request URL: `http://localhost:port/public/products`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
[
    {
        "id": "A01",
        "name": "Lego Star Wars",
        "brand": "LEGO",
        "description": "Space"
    },
    {
        "id": "A02",
        "name": "Lego Indiana Jones",
        "brand": "LEGO",
        "description": "Historical"
    },
    ...
]
```
Error Response Header:
- `500 Internal Server Error` (generic error)


### __Retrieve single product__

GET `/public/products/{productId}`

Description: Get single product

Example Request URL: `http://localhost:port/public/products/A01`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
{
    "id": "A01",
    "name": "Lego Star Wars",
    "brand": "LEGO",
    "description": "Space"
}
```
Error Response Header:
- `404 Not Found` (no product associated to productId)
- `500 Internal Server Error` (generic error)

---------------------------------
## Profiles APIs

### __Retrieve profile by email__

GET `/authenticated/profiles/{email}`

Description: Get single profile by email

Example Request URL: `http://localhost:port/authenticated/profiles/test@mail.com`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
{
    "id": 32,
    "email": "test@mail.com",
    "username": "frank_m",
    "name": "Frank",
    "surname": "White",
    "dateOfBirth": "1989-09-14"
}
```
Error Response Header:
- `404 Not Found` (no profile associated to email)
- `500 Internal Server Error` (generic error)


### __Modify profile__

PUT `/authenticated/profiles/{email}`

Description: Modify a profile

Example Request URL: `http://localhost:port/authenticated/profiles/test@mail.com`
Request body:
```
{
    "email": "test@mail.com",
    "username": "frank_m",
    "name": "Frank",
    "surname": "White",
    "dateOfBirth": "1989-09-14"
}
```
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `401 Unauthorized` (Not authorized to perform this action)
- `409 Conflicts` (email cannot be modified)
- `500 Internal Server Error` (generic error)

---------------------------------
## Tickets APIs

### __Retrieve ticket by id__

GET `/authenticated/tickets/{id}`

Description: Get single ticket by id

Example Request URL: `http://localhost:port/authenticated/tickets/2`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
{
    "id": 1,
    "productId": "A01",
    "category": "assist",
    "priority": 1,
    "description": "problem description",
    "profileId": 31,
    "expertId": null,
    "status": "OPEN"
}
```
Error Response Header:
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (no ticket associated to id)
- `500 Internal Server Error` (generic error)


### __Retrieve open tickets__

GET `/manager/tickets/open`

Description: Get list of open tickets

Example Request URL: `http://localhost:port/manager/tickets/open`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
[
    {
        "id": 1,
        "productId": "A01",
        "category": "assist",
        "priority": 1,
        "description": "problem description",
        "profileId": 31,
        "expertId": null,
        "status": "OPEN"
    },
    ...
]
```
Error Response Header:
- `401 Unauthorized` (Not authorized to perform this action)
- `500 Internal Server Error` (generic error)


### __Retrieve tickets created by profile__

GET `/client/tickets/created/{email}`

Description: Get list of tickets created by profile

Example Request URL: `http://localhost:port/client/tickets/created/test@mail.com`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
[
    {
        "id": 1,
        "productId": "A01",
        "category": "assist",
        "priority": 1,
        "description": "problem description",
        "profileId": 31,
        "expertId": null,
        "status": "OPEN"
    },
    ...
]
```
Error Response Header:
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (no profile associated to email)
- `500 Internal Server Error` (generic error)


### __Retrieve tickets assigned to profile__

GET `/tickets/created/{email}`

Description: Get list of tickets assigned to profile

Example Request URL: `http://localhost:port/tickets/assigned/test@mail.com`
Request body: _None_

Successfull Response Header: `200 OK` (success)
Response body:
```
[
    {
        "id": 1,
        "productId": "A01",
        "category": "assist",
        "priority": 1,
        "description": "problem description",
        "profileId": 31,
        "expertId": null,
        "status": "OPEN"
    },
    ...
]
```
Error Response Header:
- `404 Not Found` (no profile associated to email)
- `500 Internal Server Error` (generic error)


### __Create ticket__

POST `client/tickets`

Description: Create ticket

Example Request URL: `http://localhost:port/client/tickets/assigned/test@mail.com`
Request body:
```
{
    "productId" : "A01",
    "category": "assistance",
    "priority": 1,
    "description": "Problems",
    "profileId": 31
}
```

Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (product or profile not found)
- `500 Internal Server Error` (generic error)


### __Modify ticket priority__

PUT `tickets/manager/{id}/priority/{priority}`

Description: Modify ticket priority

Example Request URL: `http://localhost:port/manager/tickets/2/priority/3`
Request body: _None_

Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (priority value not valid)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Assign expert to ticket__

PUT `/manager/tickets/{id}/expert`

Description: Assign an expert to the ticket

Example Request URL: `http://localhost:port/manager/tickets/2/expert`
Request body:
```
{
    "email" : "test@mail.com",
    "priority": 2
}
```
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (invalid body format or invalid priority)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket or profile not found)
- `500 Internal Server Error` (generic error)


### __Stop ticket__

PUT `/manager/tickets/{id}/stop`

Description: Stop ticket progress

Example Request URL: `http://localhost:port/manager/tickets/2/stop`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket not in Progress)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Close ticket__

PUT `/expert/tickets/{id}/close`

Description: Close ticket

Example Request URL: `http://localhost:port/expert/tickets/2/close`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket already closed)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Resolve ticket__

PUT `/expert/tickets/{id}/resolve`

Description: Resolve ticket

Example Request URL: `http://localhost:port/expert/tickets/2/resolve`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket is Closed)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Reopen ticket__

PUT `/client/tickets/{id}/reopen`

Description: Reopen ticket

Example Request URL: `http://localhost:port/client/tickets/2/reopen`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket is not Closed or Resolved)
- `401 Unauthorized` (Not authorized to perform this action)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)
