# Exercise 2 - Configuring Authorization Servers and Resource Servers

Two Maven/Spring Boot projects that work together end-to-end:

- **auth-server** (port 9000) - a real OAuth2 Authorization Server (using
  [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)),
  issuing signed JWT access tokens. This is what the original exercise's
  `issuer-uri: https://issuer.example.com` was standing in for - here it's
  an actual, runnable service instead of a placeholder URL.
- **resource-server** (port 8090) - validates those JWTs and exposes
  protected endpoints.

## What changed vs. the exercise sheet, and why

- `WebSecurityConfigurerAdapter` is gone in Spring Security 6 (used by
  Spring Boot 3); both configs use `SecurityFilterChain` beans instead.
- The exercise only described the Resource Server side, pointing at a
  placeholder issuer that doesn't exist. To make this actually runnable
  and testable end-to-end, this version adds a real `auth-server` project
  next to it, using the `client_credentials` grant (service-to-service,
  no login form needed) so you can get a real token with a single curl
  command.
- `SecureController` additionally shows scope-based authorization
  (`@PreAuthorize("hasAuthority('SCOPE_message.read')")`) on a second
  endpoint, since restricting access by scope is usually the whole point
  of splitting things into an Authorization Server and Resource Server.

## How to open in IntelliJ

Open `auth-server` and `resource-server` as two separate Maven projects.

## How to run

1. Start `AuthServerApplication` (port 9000).
2. Start `ResourceServerApplication` (port 8090).
3. Request an access token from the auth server, using the registered
   `messaging-client` (client secret: `secret`):

   ```
   curl -X POST http://localhost:9000/oauth2/token \
     -u messaging-client:secret \
     -d "grant_type=client_credentials&scope=message.read"
   ```

   The response is a JSON object containing an `access_token` (a signed
   JWT) and its `expires_in` value.

4. Call the resource server with that token:

   ```
   TOKEN=paste-the-access_token-value-here

   curl -H "Authorization: Bearer $TOKEN" http://localhost:8090/secure
   curl -H "Authorization: Bearer $TOKEN" http://localhost:8090/secure/message
   ```

   Both should succeed. If you request a token without
   `scope=message.read` (or omit the header entirely), `/secure/message`
   returns `403 Forbidden` while `/secure` still succeeds with any valid
   token - that's the scope check in action.

5. You can also inspect the auth server's published signing key at
   `http://localhost:9000/oauth2/jwks` and its OIDC discovery document at
   `http://localhost:9000/.well-known/openid-configuration`.

## What to look at

- `auth-server/.../config/AuthorizationServerConfig.java` - registered
  client, signing key, and issuer settings.
- `resource-server/.../config/ResourceServerConfig.java` - the
  `oauth2ResourceServer().jwt()` setup.
- `resource-server/.../web/SecureController.java` - the two protected
  endpoints, one scope-gated.
