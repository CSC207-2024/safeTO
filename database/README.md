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

## Version History and Deletion Mechanism

The API supports version history for each key, allowing users to track changes over time. When a user updates a key using the `put` action, a new version is created and added to the version history. Each object in the version history includes a `_hidden` property that determines its visibility. To effectively "delete" a key, the API does not remove it from the database but rather marks all its versions as hidden. This approach ensures that the version history is maintained, allowing users to retrieve previous states if necessary. The use of the `_hidden` property provides a non-destructive way to manage data visibility while preserving the integrity of historical records.

## Endpoints

### Action: `get`

- **Description**: Retrieves the value associated with a specified key within a collection.
- **Endpoint**:

  ```code
  GET /get/{collection}/{key}
  ```

- **Parameters**:
  - **Mandatory**:
    - **collection**: The name of the collection.
    - **key**: The specific key to retrieve.
  - **Optional**: None.
- **Possible Responses**:
  - **HTTP 200**: `JSON: object` Successfully retrieved the latest visible version of the object.
  - **HTTP 404**: The specified key does not exist, or all versions of the object are marked as hidden.
  - **HTTP 400**: The request is missing the collection or key parameters.
  - **HTTP 405**: The request method is not allowed (e.g., if a POST request is sent).

### Action: `put`

- **Description**: Adds or updates a key-value pair in a specified collection.
- **Endpoint**:

  ```code
  PUT /put/{collection}/{key}
  ```

- **Parameters**:
  - **Mandatory**:
    - **collection**: The name of the collection.
    - **key**: The specific key to create or update.
    - **Request Body**: A JSON object containing the value to be associated with the key.
  - **Optional**: None.
- **Possible Responses**:
  - **HTTP 201**: Successfully created or updated the object.
  - **HTTP 400**: The request is missing the collection or key parameters, or the payload is invalid.
  - **HTTP 401**: Attempted to modify a restricted key.
  - **HTTP 405**: The request method is not allowed (e.g., if a GET request is sent to this endpoint).

### Action: `delete`

- **Description**: Marks a specified key as hidden, effectively deleting it from visibility.
- **Endpoint**:

  ```code
  DELETE /delete/{collection}/{key}
  ```

- **Parameters**:
  - **Mandatory**:
    - **collection**: The name of the collection.
    - **key**: The specific key to delete.
  - **Optional**: None.
- **Possible Responses**:
  - **HTTP 204**: Successfully deleted the key.
  - **HTTP 404**: The specified key does not exist.
  - **HTTP 400**: The request is missing the collection or key parameters.
  - **HTTP 405**: The request method is not allowed (e.g., if a PUT request is sent).

### Action: `history`

- **Description**: Retrieves the version history of a specific key within a collection.
- **Endpoint**:

  ```code
  GET /history/{collection}/{key}
  ```

- **Parameters**:
  - **Mandatory**:
    - **collection**: The name of the collection.
    - **key**: The specific key for which the history is requested.
  - **Optional**: None.
- **Possible Responses**:
  - **HTTP 200**: `JSON: {versions: [object], logs: []}` Successfully retrieved the version history.
  - **HTTP 404**: The specified key does not exist.
  - **HTTP 400**: The request is missing the collection or key parameters.
  - **HTTP 405**: The request method is not allowed.

### Action: `list`

- **Description**: Lists all keys in a specified collection, optionally providing pagination with a cursor.
- **Endpoint**:

  ```code
  GET /list/{collection}?cursor={cursor}
  ```

- **Parameters**:
  - **Mandatory**:
    - **collection**: The name of the collection.
  - **Optional**:
    - **cursor**: A cursor for pagination to retrieve more results.
- **Possible Responses**:
  - **HTTP 200**: `JSON: {results: [{key: string, value: object}], cursor: string?, list_complete: boolean}` Successfully listed the keys and their latest versions.
  - **HTTP 400**: The request is missing the collection parameter.
  - **HTTP 405**: The request method is not allowed (e.g., if a POST request is sent).

## Logging

The API automatically logs each operation for auditing purposes. Logs include the user, method, time, and respective collection and key for the actions taken.

## Notes

- The API restricts access to certain key names (e.g., `_logs`) to prevent unauthorized modifications.
- Make sure to always handle responses according to the HTTP status codes indicated in each section.

Feel free to reach out with any questions or for further assistance!
