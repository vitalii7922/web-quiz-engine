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

Method        | Endpoint          |Description|
------------- | -------------      |-----------|
GET           | /api/quiz/{id}     |obtain a quiz  
GET  | /api/quizzes?page={number}  |obtain list of quizzes by a page number
POST  | /api/quiz                  |add a quiz
DELETE  | /api/quiz/{id}           |delete a quiz
PUT  | /api/quiz/{id}              |update a quiz
POST  | /api/quiz/{id}/solve       |solve a quiz
GET  | /api/quiz/quizzes/completed |obtain list of completed quizzes
POST | /api/register               |register a user

### GET REQUESTS

/api/
