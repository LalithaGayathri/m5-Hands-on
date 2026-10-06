# Session 5B — Auto-documentation

## Part A — JavaDoc

### Prompt used for JavaDoc generation

Generate JavaDoc for the following method.

Rules:
- One-line summary in imperative mood.
- Describe the contract, not the implementation.
- Add @param for each parameter with its type and constraints.
- Add @return describing what is returned.
- Add @throws for each declared or unchecked exception the caller should be aware of.
- If any behaviour is not evident from the code, write "TODO" and skip it.

### Method selected

`createOrder(String customerId, BigDecimal amount)` in `OrderApi.java`.

### Verification

The generated JavaDoc was checked against the method body.

Result: No inconsistencies were found.

### Reflection

Number of JavaDoc inconsistencies caught on the first verification pass: 0


## Part B — README

### Prompt used for README generation

Draft a README for this project.

Include these sections:
- Description
- Build
- Quick Example
- Contributing
- License

Use only information supported by the project files. Do not invent features, commands, endpoints, dependencies, or behavior. If something cannot be determined from the files, write TODO.

Use MIT License as a placeholder.

Return only the README content.

### Output handling

The generated README was saved as `README.raw.md` for review before editing the final `README.md`.

### Review and editing

The generated README was reviewed against the project files. No invented features were identified.

The final `README.md` was edited to retain only information supported by the project and to include the required sections.

### Verification

The documented Makefile commands were tested:

- `make deps` — completed successfully; dependencies were already available.
- `make test` — 3 tests passed, 0 failed.
- `make coverage` — JaCoCo report generated at `coverage/index.html`.
- `make mutation` — PIT report generated at `build/reports/pitest/index.html`.

### Reflection

Number of invented features produced by the README generation: 0

## Part C — OpenAPI Specification

### Prompt used for OpenAPI generation

Generate an OpenAPI 3.0 YAML specification for the following OrderApi.java.

Requirements:
- Cover every endpoint, path, and HTTP method.
- Define request body schemas for POST/PUT endpoints where applicable.
- Define response schemas for all 2xx and 4xx responses.
- Define schemas for all DTOs used by the endpoints.
- Use only behavior and data supported by the source code.
- Do not invent fields, responses, validation rules, or endpoints.
- If something cannot be determined from the code, add TODO.
- Return only the OpenAPI YAML.

### Verification

The generated specification was checked against OrderApi.java, OrderDto.java, OrderService.java, and Order.java.

Verified:
- All 4 endpoints are present.
- All paths and HTTP methods are correct.
- OrderDto contains the correct fields and types.
- CreateOrderRequest contains customerId and amount.
- Each endpoint contains a 200 response.
- Each endpoint contains a 400 response.
- DTO schemas are defined.
- TODOs were retained where the source code did not provide enough information.

The OpenAPI specification was rendered in Swagger Editor and validated successfully.

## Part D — Reflection

### JavaDoc verification

Number of JavaDoc inconsistencies caught on the first verification pass: 0.

The generated JavaDoc for `createOrder()` was consistent with the method's parameters, return value, validation constraints, and exception behavior.

### README verification

Number of invented features produced by the README generation: 0.

The README draft was reviewed against the project files. Unsupported features or behavior were not added to the final README.

### Documentation artifact requiring the most hand editing

The README required the most hand editing because the existing README and the generated README draft had to be reviewed and combined into a clean final README containing the required Description, Build, Quick Example, Contributing, and License sections while avoiding unsupported claims.

The JavaDoc required only a small placement correction, and the OpenAPI specification mainly required verification against the Java source files and DTO definitions.