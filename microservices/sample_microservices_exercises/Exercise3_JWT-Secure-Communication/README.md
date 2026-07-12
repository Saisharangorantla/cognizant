# Exercise 3 - Using JSON Web Tokens (JWT) for Secure Communication

A single Maven/Spring Boot project: **jwt-auth-service** (port 8080).

This is a self-contained app: it has its own `/auth/login` endpoint that
checks a username/password and hands back a JWT, and a `/secure`
endpoint that only responds if you present that token. Nothing external
to run - unlike Exercise 2, there's no separate authorization server here,
because this is the "roll your own JWT" pattern rather than full OAuth2.

## What changed vs. the exercise sheet, and why

- **jjwt version**: the sheet used `io.jsonwebtoken:jjwt:0.9.1`, which is
  years out of date, unmaintained, and was the subject of a since-fixed
  CVE around weak key handling. This project uses the current, actively
  maintained `jjwt-api` / `jjwt-impl` / `jjwt-jackson` 0.12.x modules
  instead. The API shape changed a bit as a result:
  - `Jwts.builder().signWith(key)` - the signing algorithm is now
    inferred from the key type, so you don't pass a separate
    `SignatureAlgorithm` constant.
  - `Jwts.parser().verifyWith(key).build().parseSignedClaims(token)`
    replaces the old `parserBuilder()` / `parseClaimsJws()` calls.
- **WebSecurityConfigurerAdapter** is gone in Spring Security 6; this
  uses a `SecurityFilterChain` bean instead, with `SessionCreationPolicy.STATELESS`
  since there's no server-side session - the JWT itself carries identity
  on every request.
- **JwtTokenFilter is not a `@Component`.** It's constructed directly
  inside `SecurityConfig` and added via `addFilterBefore(...)`. Making it
  a Spring bean as well would cause Boot to *also* auto-register it as a
  generic servlet filter, so it would run twice per request.
- Added a working `/auth/login` endpoint (`AuthController`) and an
  in-memory user (`user` / `password`) - the exercise's classes handled
  validating and reading tokens, but didn't show how a token gets issued
  in the first place.

## How to open in IntelliJ

`File > Open...` on `jwt-auth-service/pom.xml`.

## How to run

1. Run `JwtAuthServiceApplication` (or `mvn spring-boot:run`).
2. Log in to get a token:

   ```
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"user","password":"password"}'
   ```

   Response:

   ```json
   { "accessToken": "eyJhbGciOiJIUzI1NiJ9..." }
   ```

3. Call the secure endpoint with that token:

   ```
   TOKEN=paste-the-accessToken-value-here

   curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/secure
   ```

   Without the header (or with a bad/expired token), you'll get a
   `401/403` instead of the greeting.

## What to look at

- `security/JwtTokenProvider.java` - token creation and validation with
  current jjwt 0.12.x APIs.
- `security/JwtTokenFilter.java` - reads the `Authorization` header and
  populates the `SecurityContext`.
- `config/SecurityConfig.java` - stateless filter chain wiring it all up.
- `web/AuthController.java` - the login endpoint that issues tokens.
