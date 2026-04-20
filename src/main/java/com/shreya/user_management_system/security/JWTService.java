package com.shreya.user_management_system.security;
import com.shreya.user_management_system.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    //Secret key....
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    //generate token....
    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",user.getRoles().stream().map(role -> role.getName()).toList());

        return Jwts.builder().setClaims(claims).setSubject(user.getUsername()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(getSigningKey()).compact();
    }

    //extract username ....
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //extract roles....
    public List<String> extractRoles(String token){
        return extractAllClaims(token).get("roles", List.class);
    }

    // validate token
    public boolean isTokenValid(String token,String username){
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // extract claim(Generic)
    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // extract all claims.....
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // check expiry....
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Signing key....
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}