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

## Run web quiz engine

gradle bootRun

## Run the tests

gradle clean test

## REST API 

Requests to API:

Method        | Endpoint     |
------------- | -------------|
GET           | /api/quiz/{id}|  
GET  | /api/quizzes/page={number}  |
POST  | /api/quiz  |
DELETE  | /api/quiz/{id}  |
PUT  | /api/quiz/{id}  |
POST  | /api/quiz/{id}/solve  |

### GET REQUESTS

/api/
