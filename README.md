# Web quiz engine REST API application

This is a simple CRUD application.
Web quiz engine provides services for users such as:
* add quizzes
* obtain a list of quizzes(with pagination)
* update a quiz
* delete a quiz
* solve a quiz
* obtain list of solved(completed) quizzes(with pagination)
* user's authorization
* user's registration

Used technologies:
* Java SDK version 11.0.7
* Spring Boot v. 2.2.2
* H2 database(integrated database to SPring Boot)
* Spring security test v. 5.1.6
* Lombok v. 1.8.12
* JavaScript version 1.8.5
* Bootstrap v. 4.5.2

## Run web quiz engine

gradle bootRun

## Run the tests

gradle clean test

## REST API 

Requests to API:

Method        | Endpoint          |Description|
------------- | -------------      |-----------|
GET           | /api/quizzes/{id}     |get a quiz  
GET  | /api/quizzes?page  |get list of quizzes by a page number
POST  | /api/quizzes                  |post a quiz
DELETE  | /api/quizzes/{id}           |delete a quiz
PUT  | /api/quizzes/{id}              |update a quiz
POST  | /api/quizzes/{id}/solve       |solve a quiz
GET  | /api/quizzes/completed?page |get list of completed quizzes
POST | /api/register               |register a user

## EXAMPLES OF REQUESTS
### Request

    `POST /api/register`

    http://localhost:8889/api/register
    
    {
      "email": "ivanov@gmail.com",
      "password": "12345"
    }

### Response

    Status: 200 OK
__________
### Request

    `POST /api/register`

    http://localhost:8889/api/register
    
    {
     "email": "ivanov@gmail.com",
     "password": "12345"
    }

### Response

    {
    "timestamp": "2020-10-28T13:30:18.030+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Account with email ivanov@gmail.com already exists",
    "path": "/api/register"
    }
__________  
**Each consequent request must be send with basic authorization(email and password)**

### Request

    `GET /api/register`

    http://localhost:8889/api/quiz
    
    {
     "email": "ivanov@gmail.com",
     "password": "12345"
    }

### Response

    {
    "timestamp": "2020-10-28T13:30:18.030+0000",
    "status": 400,
    "error": "Bad Request",
    "message": "Account with email ivanov@gmail.com already exists",
    "path": "/api/register"
    }
__________  
### Request
    
        `GET /api/quizzes/2150`
    
        http://localhost:8889/api/quiz
        
        {
         "email": "ivanov@gmail.com",
         "password": "12345"
        }
 
### Response
    
        {
            "id": 2150,
            "title": "Countries",
            "text": "What countries are in Europe?",
            "options": [
                "Belgium",
                "Germany",
                "Japan"
            ],
            "modifiable": false
        }
__________  
### Request
    
        `POST /api/quizzes`
    
        http://localhost:8889/api/quizzes
        
        {
        "title": "Cities",
        "text": "What cities are capitals?",
        "options": ["Moscow", "Manchester", "Leipcig", "Berlin"],
        "answer": [0, 3]
        }
      
### Response
    
        {
            "id": 2760,
            "title": "Cities",
            "text": "What cities are capitals",
            "options": [
                "Moscow",
                "Manchester",
                "Leipcig",
                "Berlin"
            ],
            "modifiable": true
        }
        
__________        
### Request
    
        `PUT /api/quizzes/2760`
    
        http://localhost:8889/api/quizzes
        
        {
        "title": "Cities",
        "text": "What cities are capitals?",
        "options": ["Moscow", "Manchester", "Leipcig", "Berlin", "Paris"],
        "answer": [0, 3, 4]
        }
    
### Response
    
        {
            "id": 2760,
            "title": "Cities",
            "text": "What cities are capitals?",
            "options": [
                "Moscow",
                "Manchester",
                "Leipcig",
                "Berlin"
            ],
            "modifiable": true
        }
__________        
### Request
            
        `GET /api/quizzes?page=1`
            
         http://localhost:8889/api/quizzes
                
          {
          "title": "Cities",
           "text": "What cities are capitals?",
           "options": ["Moscow", "Manchester", "Leipcig", "Berlin", "Paris"],
           "answer": [0, 3, 4]
           }
### Response                
            
       {
           "content": [
               {
                   "id": 1989,
                   "title": "Countries",
                   "text": "What countries are in Europe?",
                   "options": [
                       "Great Britain",
                       "Germany",
                       "Canada",
                       "China"
                   ],
                   "modifiable": false
               },
               {
                   "id": 2181,
                   "title": "Fruits",
                   "text": "What plants are fruits?",
                   "options": [
                       "Apple",
                       "Banana"
                   ],
                   "modifiable": true
               },
           ],
           "pageable": {
               "sort": {
                   "sorted": true,
                   "unsorted": false,
                   "empty": false
               },
               "offset": 0,
               "pageSize": 2,
               "pageNumber": 0,
               "paged": true,
               "unpaged": false
           },
           "totalElements": 2,
           "totalPages": 1,
           "last": false,
           "size": 5,
           "number": 0,
           "sort": {
               "sorted": true,
               "unsorted": false,
               "empty": false
           },
           "numberOfElements": 2,
           "first": true,
           "empty": false
       }
# UI version
To open the starter page http://localhost:8889 
## UI screenshots
![picture alt](https://drive.google.com/drive/folders/11ivwCmUUdEpHhR0ZVv5K7j_Ix_xjR1vu "Title is optional")