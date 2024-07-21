# safeTO/backend: REST API

The API will be available at <https://csc207-api.joefang.org>.

## Location-based Services

### Resolve location

Endpoint: `/lookup`
Method: GET
Parameters: `lat`: float, `long`
Returns: A RESTful JSON object, with

```
data = {
    _place: A types.Place object, which has detailed information about the place; the frontend can 
    postalCode: A string representing the postal code
}
```
