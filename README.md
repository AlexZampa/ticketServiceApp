# ticketServiceApp

## List of APIs
- [Products APIs](#products-apis)
  - [Retrieve all products](#retrieve-all-products)
  - [Retrieve-single-product](#retrieve-single-product)
- [Profile APIs](#profiles-apis)
  - [Retrieve profile by email](#retrieve-profile-by-email)
  - [Create profile](#create-profile)
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

## Products APIs

### __Retrieve all products__

GET `/products`

Description: Get all the products

Example Request URL: `http://localhost:port/products`
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

GET `/products/{productId}`

Description: Get single product

Example Request URL: `http://localhost:port/products/A01`
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

GET `/profiles/{email}`

Description: Get single profile by email

Example Request URL: `http://localhost:port/profiles/test@mail.com`
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


### __Create profile__

POST `/profiles`

Description: Create a profile

Example Request URL: `http://localhost:port/profiles`
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
- `409 Conflicts` (profile with same email already exists)
- `500 Internal Server Error` (generic error)


### __Modify profile__

PUT `/profiles/{email}`

Description: Modify a profile

Example Request URL: `http://localhost:port/profiles/test@mail.com`
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
- `409 Conflicts` (email cannot be modified)
- `500 Internal Server Error` (generic error)

---------------------------------
## Tickets APIs

### __Retrieve ticket by id__

GET `/tickets/{id}`

Description: Get single ticket by id

Example Request URL: `http://localhost:port/tickets/2`
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
- `404 Not Found` (no ticket associated to id)
- `500 Internal Server Error` (generic error)


### __Retrieve open tickets__

GET `/tickets/open`

Description: Get list of open tickets

Example Request URL: `http://localhost:port/tickets/open`
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
- `500 Internal Server Error` (generic error)


### __Retrieve tickets created by profile__

GET `/tickets/created/{email}`

Description: Get list of tickets created by profile

Example Request URL: `http://localhost:port/tickets/created/test@mail.com`
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

POST `/tickets`

Description: Create ticket

Example Request URL: `http://localhost:port/tickets/assigned/test@mail.com`
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
- `404 Not Found` (product or profile not found)
- `500 Internal Server Error` (generic error)


### __Modify ticket priority__

PUT `tickets/{id}/priority/{priority}`

Description: Modify ticket priority

Example Request URL: `http://localhost:port/tickets/2/priority/3`
Request body: _None_

Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (priority value not valid)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Assign expert to ticket__

PUT `tickets/{id}/expert`

Description: Assign an expert to the ticket

Example Request URL: `http://localhost:port/tickets/2/expert`
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
- `404 Not Found` (ticket or profile not found)
- `500 Internal Server Error` (generic error)


### __Stop ticket__

PUT `/tickets/{id}/stop`

Description: Stop ticket progress

Example Request URL: `http://localhost:port/tickets/2/stop`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket not in Progress)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Close ticket__

PUT `/tickets/{id}/close`

Description: Close ticket

Example Request URL: `http://localhost:port/tickets/2/close`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket already closed)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Resolve ticket__

PUT `/tickets/{id}/close`

Description: Resolve ticket

Example Request URL: `http://localhost:port/tickets/2/resolve`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket is Closed)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)


### __Reopen ticket__

PUT `/tickets/{id}/reopen`

Description: Reopen ticket

Example Request URL: `http://localhost:port/tickets/2/reopen`
Request body: _None_
Successfull Response Header: `201 Created` (success)
Response body: _None_

Error Response Header:
- `400 Bad Request` (Ticket is not Closed or Resolved)
- `404 Not Found` (ticket not found)
- `500 Internal Server Error` (generic error)
