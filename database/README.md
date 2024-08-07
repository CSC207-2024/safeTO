# safeTO-DB API Documentation

This document outlines the functionalities of the safeTO-DB API, a lightweight key-value database implementation using Cloudflare KV. It is designed to organize data into collections, providing a simple interface for CRUD operations while ensuring reasonable isolation between collections.

## Getting Started

To run the safeTO-DB API locally:

- Execute `npm run dev` in your terminal to start the development server.
- Open your browser and navigate to `http://localhost:8787/` to see the API in action.
- To publish your worker, run `npm run deploy`.

Learn more about Cloudflare Workers at [Cloudflare Workers Documentation](https://developers.cloudflare.com/workers/).

## Authentication

Every request to the API endpoints must be authenticated using an access token. To authenticate a request, include the following HTTP header:

```code
authorization: Bearer <put_your_api_key_here>
```

Replace `<put_your_api_key_here>` with the actual API token sent to your email. **Do not store the token directly in your code.** Instead, set it as an environment variable named `SAFETO_DB_TOKEN`. If you suspect that your API key has been compromised, please contact us immediately at [i@joefang.org](mailto:i@joefang.org) for assistance in revoking and re-issuing your API key.

## Endpoints

### Action: `get`

- **Description**: Retrieves the value associated with a specified key within a collection.
- **Endpoint**:

  ```code
  GET /get/{collection}/{key}
  ```

- **Possible Responses**:
  - **HTTP 200**:
    - **Description**: Successfully retrieved the latest visible version of the object.
    - **Response Format**: Returns the object in JSON format, excluding the `_hidden` property.
  - **HTTP 404**:
    - **Description**: The specified key does not exist, or all versions of the object are marked as hidden.
    - **Response Format**: An explanation message such as "Not Found".
  - **HTTP 400**:
    - **Description**: The request is missing the collection or key parameters.
    - **Response Format**: An explanation message such as "Missing collection or key".
  - **HTTP 405**:
    - **Description**: The request method is not allowed (e.g., if a POST request is sent).
    - **Response Format**: An explanation message such as "Method Not Allowed".

### Action: `put`

- **Description**: Adds or updates a key-value pair in a specified collection.
- **Endpoint**:

  ```code
  PUT /put/{collection}/{key}
  ```

- **Possible Responses**:
  - **HTTP 201**:
    - **Description**: Successfully created or updated the object.
    - **Response Format**: A confirmation message such as "Created".
  - **HTTP 400**:
    - **Description**: The request is missing the collection or key parameters, or the payload is invalid.
    - **Response Format**: An explanation message, such as "Missing collection or key" or "Invalid payload".
  - **HTTP 401**:
    - **Description**: Attempted to modify a restricted key.
    - **Response Format**: An explanation message such as "Invalid key - unauthorized to modify this key".
  - **HTTP 405**:
    - **Description**: The request method is not allowed (e.g., if a GET request is sent to this endpoint).
    - **Response Format**: An explanation message such as "Method Not Allowed".

### Action: `delete`

- **Description**: Marks a specified key as hidden, effectively deleting it from visibility.
- **Endpoint**:

  ```code
  DELETE /delete/{collection}/{key}
  ```

- **Possible Responses**:
  - **HTTP 204**:
    - **Description**: Successfully deleted the key.
    - **Response Format**: No content is returned.
  - **HTTP 404**:
    - **Description**: The specified key does not exist.
    - **Response Format**: An explanation message such as "Not Found".
  - **HTTP 400**:
    - **Description**: The request is missing the collection or key parameters.
    - **Response Format**: An explanation message such as "Missing collection or key".
  - **HTTP 405**:
    - **Description**: The request method is not allowed (e.g., if a PUT request is sent).
    - **Response Format**: An explanation message such as "Method Not Allowed".

### Action: `history`

- **Description**: Retrieves the version history of a specific key within a collection.
- **Endpoint**:

  ```code
  GET /history/{collection}/{key}
  ```

- **Possible Responses**:
  - **HTTP 200**:
    - **Description**: Successfully retrieved the version history.
    - **Response Format**: A JSON object containing an array of visible versions and logs.
  - **HTTP 404**:
    - **Description**: The specified key does not exist.
    - **Response Format**: An explanation message such as "Not Found".
  - **HTTP 400**:
    - **Description**: The request is missing the collection or key parameters.
    - **Response Format**: An explanation message such as "Missing collection or key".
  - **HTTP 405**:
    - **Description**: The request method is not allowed.
    - **Response Format**: An explanation message such as "Method Not Allowed".

### Action: `list`

- **Description**: Lists all keys in a specified collection, optionally providing pagination with a cursor.
- **Endpoint**:

  ```code
  GET /list/{collection}?cursor={cursor}
  ```

- **Possible Responses**:
  - **HTTP 200**:
    - **Description**: Successfully listed the keys and their latest versions.
    - **Response Format**: A JSON object with the list of visible keys, their latest versions, and a cursor for pagination.
  - **HTTP 400**:
    - **Description**: The request is missing the collection parameter.
    - **Response Format**: An explanation message such as "Missing collection".
  - **HTTP 405**:
    - **Description**: The request method is not allowed (e.g., if a POST request is sent).
    - **Response Format**: An explanation message such as "Method Not Allowed".

## Logging

The API automatically logs each operation for auditing purposes. Logs include the user, method, time, and respective collection and key for the actions taken.

## Notes

- The API restricts access to certain key names (e.g., `_logs`) to prevent unauthorized modifications.
- Make sure to always handle responses according to the HTTP status codes indicated in each section.
