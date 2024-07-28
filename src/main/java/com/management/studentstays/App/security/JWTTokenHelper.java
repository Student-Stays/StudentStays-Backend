package com.management.studentstays.App.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenHelper {

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  private String secret =
      "jwtTokenKeyffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

  // retrieve userName from JWT token
  public String getUserNameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  // retrieve expiration date from JWT token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  // for retrieving information from the token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  // check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  // generate token for the user
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /* while creating the token -
  	1. Define claims of the token, like Issuer, Expiration, Subject and the ID
  	2. Sign the JWT using the HS512 algorithm and secret key.
  	3. According to JWS Compact Serialization
  */
  private String doGenerateToken(Map<String, Object> claims, String subject) {
    long currentTimeMillis = System.currentTimeMillis();
    Date issuedAt = new Date(currentTimeMillis);
    Date expiration = new Date(currentTimeMillis + JWT_TOKEN_VALIDITY * 100);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String userName = getUserNameFromToken(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
