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
GET           | /api/quiz/{id}     |get a quiz  
GET  | /api/quizzes?page={number}  |get list of quizzes by a page number
POST  | /api/quiz                  |post a quiz
DELETE  | /api/quiz/{id}           |delete a quiz
PUT  | /api/quiz/{id}              |update a quiz
POST  | /api/quiz/{id}/solve       |solve a quiz
GET  | /api/quiz/quizzes/completed |get list of completed quizzes
POST | /api/register               |register a user

### GET REQUESTS

/api/
