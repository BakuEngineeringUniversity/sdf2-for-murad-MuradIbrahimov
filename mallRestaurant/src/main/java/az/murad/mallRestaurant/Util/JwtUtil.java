package az.murad.mallRestaurant.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateToken(String username, String password) {
        return Jwts.builder()
                .setSubject(username)
                .claim("password", password)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration != null && expiration.after(new Date())) {
                return claims.getSubject();
            }
        } catch (Exception e) {
            // Handle parsing or expired token exception
        }

        return null; // Invalid token or expired
    }

    // Add methods for additional token validation, expiration check, etc.
}
