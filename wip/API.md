# safeTO/backend: REST API

The API will be available at <https://csc207-api.joefang.org>.

## Location-based Services

### Resolve location

Endpoint: `/location/resolve`
Method: POST
Parameters: latitude, longitude
Returns: a HS256 signed location object.