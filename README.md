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
* Java version 12.0.2
* Spring Boot v. 2.2.2
* H2 database(integrated database to Spring Boot)
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
DELETE  | /api/quizzes/{id}           |delete a quiz(can be deleted by the user who created it)
PUT  | /api/quizzes/{id}              |update a quiz(can be updated by the user who created it)
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
# User Interface
To open the starter page http://localhost:8889 
## UI screenshots
Sign In
![signIn](https://user-images.githubusercontent.com/51421459/97480019-ea66e280-1963-11eb-846e-19ab3bf03733.jpg)
Submit form
![submit](https://user-images.githubusercontent.com/51421459/97481284-a248bf80-1965-11eb-8856-eba4b847171f.jpg)
![submitted](https://user-images.githubusercontent.com/51421459/97481289-a379ec80-1965-11eb-8d24-5e8cf472a1ce.jpg)
List of quizzes
![list](https://user-images.githubusercontent.com/51421459/97481623-23a05200-1966-11eb-89f9-c96c33b2bdae.jpg)
Deletion
![deletion](https://user-images.githubusercontent.com/51421459/97481631-256a1580-1966-11eb-9e05-645ba1d6f944.jpg)
