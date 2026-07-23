# REST - Get Country Based On Country Code

## Endpoint

- URL: `http://localhost:8083/countries/{code}`
- Method: `GET`
- Controller: `com.cognizant.springlearn.controller.CountryController`
- Method: `getCountry(String code)`

## Sample Request

- `http://localhost:8083/country/in`

## Sample Response

```json
{
  "code": "IN",
  "name": "India"
}
```

## Implementation Notes

- The controller reads country code with `@PathVariable`.
- `CountryService.getCountry(String code)` loads `country.xml` and fetches `countries` list.
- Country code matching is case-insensitive via `equalsIgnoreCase`.
- If no match is found, API returns `404 Not Found`.

## Run

```bash
mvn clean test
mvn spring-boot:run
```
