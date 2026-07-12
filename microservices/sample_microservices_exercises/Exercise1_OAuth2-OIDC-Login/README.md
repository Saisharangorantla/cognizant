# Exercise 1 - Centralized Authentication with OAuth 2.1 / OIDC

A single Maven/Spring Boot project: **oidc-client-app** (port 8080).

## What changed vs. the exercise sheet, and why

- `WebSecurityConfigurerAdapter` was **removed** in Spring Security 5.7+
  (it doesn't exist at all in the Spring Security 6.x that ships with
  Spring Boot 3). `SecurityConfig` here uses the current replacement: a
  `SecurityFilterChain` `@Bean` built with the lambda DSL.
- Instead of manually listing Google's authorization/token/user-info
  endpoints (as in the exercise), the config uses Spring Security's
  built-in `google` provider (`CommonOAuth2Provider`), which already
  knows those URLs. You only need to supply a client id/secret. The
  `application.yml` has a commented-out example showing the
  "generic OIDC issuer" style too, for providers like Keycloak/Okta/Auth0.
- `UserController` asks for an `OidcUser` instead of a raw `Principal`,
  so it can return meaningful profile fields (name, email, subject)
  rather than a generic object dump.

## Getting real credentials

To actually log in you need a real OAuth2 client registration:

1. Go to the [Google Cloud Console](https://console.cloud.google.com/apis/credentials),
   create an OAuth 2.0 Client ID of type "Web application".
2. Add `http://localhost:8080/login/oauth2/code/google` as an authorized
   redirect URI.
3. Copy the generated client ID/secret into
   `src/main/resources/application.yml`, replacing
   `YOUR_GOOGLE_CLIENT_ID` / `YOUR_GOOGLE_CLIENT_SECRET`.

(If you don't want to use Google, swap in the generic OIDC provider block
that's commented out in `application.yml`, pointed at any OIDC-compliant
issuer, e.g. a local Keycloak instance.)

## How to open in IntelliJ

`File > Open...` on `oidc-client-app/pom.xml`.

## How to run

1. Run `OidcClientAppApplication` (or `mvn spring-boot:run`).
2. Open `http://localhost:8080/user` in a browser. Since you're not
   authenticated yet, Spring Security redirects you to Google's login
   page.
3. After signing in and granting consent, you're redirected back and
   `/user` returns your profile as JSON (subject, name, email, and the
   full claims map).

## What to look at

- `config/SecurityConfig.java` - the `oauth2Login()` setup.
- `web/UserController.java` - reading the authenticated `OidcUser`.
- `src/main/resources/application.yml` - the client registration.
