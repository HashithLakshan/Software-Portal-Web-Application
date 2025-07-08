package Software.Portal.web.Security.jwt;

import Software.Portal.web.Security.Services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${toolbox.app.jwtSecret}")
    private String jwtSecret;

    @Value("${toolbox.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Generate secret signing key from base64 encoded string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Generate a JWT token with username and roles
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get username (subject) from JWT token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Validate JWT token integrity and expiration
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Optional: If you still need to extract embedded token inside a custom `authToken` header
//    public String getSubjectFromCustomAuthToken(String token) {
//        try {
//            // Decode the first part (header) of the token
//            String[] parts = token.split("\\.");
//            if (parts.length != 3) {
//                logger.error("Invalid JWT structure");
//                return null;
//            }
//
//            String headerJson = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));
//            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
//            com.fasterxml.jackson.databind.JsonNode headerNode = mapper.readTree(headerJson);
//
//            if (!headerNode.has("authToken")) {
//                logger.error("No 'authToken' field in JWT header");
//                return null;
//            }
//
//            String embeddedToken = headerNode.get("authToken").asText();
//
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(embeddedToken)
//                    .getBody();
//
//            return claims.getSubject();
//
//        } catch (Exception e) {
//            logger.error("Failed to parse custom authToken in JWT header: {}", e.getMessage());
//            return null;
//        }
//    }

    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Make sure this is correct
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // "h"
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

}
