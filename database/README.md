# Database API

This is a lightweight key-value (KV) database API implementation using Cloudflare KV as the database provider. It takes inspiration from MongoDB's concept of collections, so each item belongs to a collection—or a "namespace" for those familiar with C++ programming. The database ensures reasonable isolation between collections, so average users cannot access data across collections.

The API is accessible at <https://csc207-db.joefang.org/>. Every request must be authenticated using the access token that has been sent to your emails. Please NEVER store the token in your code; instead, set it as an environment variable. For interoperability, please use `SAFETO_DB_TOKEN` as the name of the environment variable.

## Action: `get`

Endpoint:

## Action: `put`

## Action: `delete`

## Action: `history`

## Action: `list`
