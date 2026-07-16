package com.acme.jwtauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Creates and validates the app's own JWTs. This mirrors the exercise's
 * JwtTokenProvider, but uses the current jjwt 0.12.x API:
 * <ul>
 *   <li>{@code Jwts.builder()...signWith(key)} - the algorithm is now
 *       inferred from the key type, so there's no need to pass a
 *       {@code SignatureAlgorithm} constant separately.</li>
 *   <li>{@code Jwts.parser().verifyWith(key).build().parseSignedClaims(token)}
 *       replaces the old {@code parserBuilder()/parseClaimsJws()} calls.</li>
 * </ul>
 */
@Component
public class JwtTokenProvider {

    private static final long VALIDITY_IN_MILLIS = 3_600_000; // 1 hour
    private static final String ROLES_CLAIM = "roles";

    private final SecretKey signingKey;

    public JwtTokenProvider(SecretKey jwtSigningKey) {
        this.signingKey = jwtSigningKey;
    }

    public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLIS);

        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim(ROLES_CLAIM, roles)
                .issuedAt(now)
                .expiration(validity)
                .signWith(signingKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        String rolesClaim = claims.get(ROLES_CLAIM, String.class);

        List<GrantedAuthority> authorities = (rolesClaim == null || rolesClaim.isBlank())
                ? List.of()
                : Arrays.stream(rolesClaim.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        // Password is irrelevant post-authentication - the token itself is the credential.
        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
