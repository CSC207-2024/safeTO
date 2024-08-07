# Database API

This is a lightweight key-value (KV) database API implementation using Cloudflare KV as the database provider. It takes inspiration from MongoDB's concept of collections, so each item belongs to a collection—or a "namespace" for those familiar with C++ programming. The database ensures reasonable isolation between collections, so average users cannot access data across collections.

The API is accessible at <https://csc207-db.joefang.org/>. Every request must be authenticated using the access token that has been sent to your emails. Please NEVER store the token in your code; instead, set it as an environment variable. For interoperability, please use `SAFETO_DB_TOKEN` as the name of the environment variable.

## Authentication

Every request to the API endpoints below must be authenticated using the access token. To authenticate the request, simply put a HTTP header

`authorization: Bearer <put_your_api_key_here>`

Replace the `<put_your_api_key_here>` with the actual API token that is sent to your email. If you have reason to believe that your API key has been compromised, please contact me at <i@joefang.org> ASAP so that I can revoke and re-issue the API key for you.

## Action: `get`

* Endpoint: `/get/{collection}/{key}`
* Return:

## Action: `put`

## Action: `delete`

## Action: `history`

## Action: `list`
