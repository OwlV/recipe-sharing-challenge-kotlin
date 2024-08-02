# Recipe Sharing Platform
**Backend Coding Challenge**

## Introduction

Your task is to build the backend functionality for a Recipe Sharing Platform. The platform allows users to create, share, and search for recipes. Users can create an account, log in, and perform various operations such as creating, updating, and deleting recipes, as well as searching for recipes based on different criteria.

## Requirements

### User Authentication

1. Implement user registration with email, username, and password.
2. All email addresses are expected to be unique in the database
3. The user table contains a key column with unique keys. 
4. Ensure password hashing and storage for security purposes.
 

### Recipe Management

1. Implement CRUD (Create, Read, Update, Delete) operations for recipes.
2. A recipe should have the following attributes:
    * Title (required)
    * User-ID (required)
    * Description (optional)
    * Ingredients (required, comma-separated values)
    * Instructions (required)
    * Servings (optional)
3. Ensure that the ingredients use the metric system.
4. Provide an endpoint to retrieve a list of recipes created by a specific user.
5. All recipe management endpoints require authentication.

#### Ingredients

When looking at the ingredients, we see a 1:n relationship between the recipe and ingredients. Therefore, it's recommended to manage ingredients in a second table with at least four columns:
* recipe_id
* value
* unit
* type

_Useful Units_

| unit  | comment    |
|-------|------------|
| g     | Gram       |
| kg    | Kilogram   |
| ml    | Milliliter |
| l     | Liter      |
| pc    | Piece      |
| tsp   | Teaspoon   |
| tbsp  | Tablespoon |
| pinch | A dash     |

_Examples_

| value | unit | type |
|-------|------|------|
| 8     | pcs  | egg  |
| .05   | l    | milk |
| 1     | dash | salt |

## Recipe Search

1. Implement a search functionality that allows users to search for recipes based on the following criteria:
    * Username (exact match or partial match)
    * Title (exact match or partial match)
2. Return a list of matching recipes based on the search criteria.
3. All recipe search endpoints require authentication.

## Recipe Recommendations

We want you to implement a special endpoint that returns a single recipe recommendation based on the current weather conditions.

### Setting up an account

To enable this feature you first need to setup an account on https://www.visualcrossing.com/

There are multiple free weather APIs on the market. This one allows free registration without any payment details required for the free tier.
Also the limit of 1000 free records per day should be sufficient for all evaluation purposes.

### The algorithm

Implement a "recommendations" functionality that recommends a recipe based on the current weather forecast. The recommendations work as follows:

1. Fetch the current weather situation in BERLIN (Germany) from the API
2. Avoid recipes that require baking if the outside temperature is above t1 degrees Celsius.
3. Avoid recipes using frozen ingredients if the outside temperature is below t2 degrees Celsius.
4. From the remaining recipes do a random choice.

The following constant values for t1,t2 shall be used, encoded in the application.yml:

| Variable | value |
|----------|-------|
| t1       | 28°C  |
| t2       | 2°C   |

Alternatively the values for t1 and t2 can also be given as parameters to the API call.
In this case the query parameters override the default values.

## Implementation

* Use Kotlin and the Spring Boot Framework for the implementation.
* Use a database of your choice to store the user and recipe information.
* Pay attention to error handling and validation of incoming data.

## API Documentation

Include clear and concise documentation for all endpoints, including request/response examples and any additional information required to understand and use the API.

## Testing

Write unit tests for the implemented functionality to ensure proper operation and behavior.

## Submission

* Provide a GitHub repository containing your code.
* Include a `SETUP.md` file with instructions on how to set up and run your application.
* Optionally, you can deploy your application to a hosting service and provide a link to the live demo.

## Evaluation Criteria

* Implementation of all the required features and functionalities.
* Code structure, readability, and maintainability.
* Proper usage of HTTP status codes and RESTful principles.
* Error handling and validation of incoming data.
* Test coverage and quality of unit tests.
* Documentation clarity and completeness.

## Note

* Focus on implementing the backend functionality only. No need to spend time on frontend/UI development for this challenge.
* Read the `ABOUT.md` for more details about this source code.


### API Description ### 
## Recipe Management API Documentation ##

This documentation provides detailed information about the Recipe Management API endpoints. Note that all endpoints, except for signup and login, require a valid JWT token for access.
Token should be provided in HttpHeader with name "Authorization" and value "Bearer: _token value_"

## Base URL
`http://localhost:8080`

## Authentication

### Signup
**Endpoint:** `/auth/signup`

**Method:** `POST`

**Description:** Create a user account.

**Request Body:**
```json
{
  "username": "string", - not blank unique value
  "password": "string", - not blank 
  "email": "string" - not blank unique value
}
```
**Responses:**

* 200 OK: User was successfully created.
* 400 Bad Request: Invalid input parameters.
* 500 Internal Server Error: Internal server error.

### Login
**Endpoint:** /auth/login

**Method:** `POST`

**Description:** Log in to an existing user account.

**Request Body:**
```json
{
  "username": "string", - not blank string value
  "password": "string" - not blank string value
}
```
**Responses:**

* 200 OK: User was successfully authenticated.
* 400 Bad Request: Invalid input parameters.
* 401 Unauthorized: Invalid username or password.
* 500 Internal Server Error: Internal server error.

## Recipe Management

### Fetch Recipe by ID
**Endpoint:** /api/recipe/{id}

**Method:** GET

**Description:** Fetch a recipe by its ID.

**Parameters:** 

id (path, required): The ID of the recipe to fetch.
**Responses:**

* 200 OK: Recipe found.
* 404 Not Found: Matched recipes not found.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.
Update Recipe
**Endpoint:** /api/recipe/{id}

**Method:** PUT

**Description:** Update attributes of a recipe with the given ID.

**Parameters:** 

* id (path, required): The ID of the recipe to update.
* Request Body:

```json
{
  "title": "string", - not blank
  "description": "string", - not blank
  "ingredients": "string", - comma separeted string of "amount(double) unit(g|kg|ml|l|pc|tsp|tbsp|pinch) type(string)"
  "instructions": "string", - not blank
  "serving": "integer" - can be omitted, should be positibe when exists
}
```
**Responses:**

* 200 OK: Recipe updated.
* 400 Bad Request: Invalid client request.
* 401 Unauthorized: User is not authorized.
* 403 Forbidden: Authorized user is not the author of the recipe.
* 500 Internal Server Error: Internal server error.

### Delete Recipe
**Endpoint:** /api/recipe/{id}

**Method:** DELETE

**Description:** Delete the recipe with the given ID.

**Parameters:** 

* id (path, required): The ID of the recipe to delete.

**Responses:**

* 200 OK: Recipe deleted.
* 400 Bad Request: Invalid client request.
* 401 Unauthorized: User is not authorized.
* 403 Forbidden: Authorized user is not the author of the recipe.
* 500 Internal Server Error: Internal server error.

### Create Recipe
**Endpoint:** /api/recipe/

**Method:** POST

**Description:** Create a new recipe with the authorized user as the author.

* Request Body:

```json
{
   "title": "string", - not blank
   "description": "string", - not blank
   "ingredients": "string", - comma separeted string of "amount(double) unit(g|kg|ml|l|pc|tsp|tbsp|pinch) type(string)"
   "instructions": "string", - not blank
   "serving": "integer" - can be omitted, should be positibe when exists
}
```
**Responses:**

* 200 OK: Recipe created.
* 400 Bad Request: Invalid client request.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.

## Recipe Search

### Get Recommended Recipe

**Endpoint:** /api/recipe/search/recommended

**Method:** GET

**Description:** Get a recommended recipe depending on the current weather in Berlin. Avoids baking when it's too hot and frozen ingredients when it's too cold.

**Responses:**

* 200 OK: Recommended recipe found.
* 404 Not Found: Matched recipes not found.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.
### Search Recipes by Title
**Endpoint:** /api/recipe/search/byTitle

**Method:** GET

**Description:** Search recipes by partial or full title match.

**Parameters:** 

* title (query, required): The title or part of the title to search for.

**Responses:**

* 200 OK: Recipes found.
* 404 Not Found: Matched recipes not found.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.
### Search Recipes by Author
**Endpoint:** /api/recipe/search/byAuthor

**Method:** GET

**Description:** Search recipes by partial or full username (author) match.

**Parameters:** 

* author (query, required): The username or part of the username to search for.

**Responses:**


* 200 OK: Recipes found.
* 404 Not Found: Matched recipes not found.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.
Fetch All Recipes by User
**Endpoint:** /api/recipe/byUserName

**Method:** GET

**Description:** Fetch all recipes created by a particular user.

**Parameters:** 

* username (query, required): The username of the user whose recipes to fetch.

**Responses:**

* 200 OK: Recipes found.
* 404 Not Found: Matched recipes not found.
* 401 Unauthorized: User is not authorized.
* 500 Internal Server Error: Internal server error.
Components

## Schemas
### RecipeDto

```json
{
   "id" : "integer"
   "title": "string", - not blank
   "description": "string", - not blank
   "ingredients": "string", - comma separeted string of "amount(double) unit(g|kg|ml|l|pc|tsp|tbsp|pinch) type(string)"
   "instructions": "string", - not blank
   "serving": "integer" - can be omitted, should be positibe when exists
}
```
### ErrorDetails

```json
{
  "errorMessage": "string",
  "errorTimestamp": "string"
}
```
### Registration

```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```
### RegistrationResult

```json
{
  "username": "string",
  "email": "string",
  "details": "string"
}
```
### Login

```json
{
  "username": "string",
  "password": "string"
}
``` 