package com.heavens.stream.configuration.jwtUtil;

import com.heavens.stream.models.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class JwtUtil {

    private static final String SECRET_KEY = "7120729F747A91FD57A4E97AB8855DBF4DB761ECF26734665C5102F9E8D9E8BE"; // Replace with a secure secret key

    public static String generateToken(MyUserDetails myUserDetails) {
        return generateToken(new HashMap<>(), myUserDetails);
    }

    public static String generateToken(Map<String, Object> extractClaims, MyUserDetails myUserDetails) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(myUserDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 24))     // Set token expiration (24 hours)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    public static String extractUsername(String token) {
       return extractClaims(token, Claims::getSubject);
    }

    private static Key getSigningKey() {
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public static boolean validateToken(String token, MyUserDetails myUserDetails){
        String username =extractUsername(token);
        return username.equalsIgnoreCase(myUserDetails.getUsername()) && !isTokenExpired(token);
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) {
        return extractClaims(token,Claims::getExpiration);
    }

    private static Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    public static  <T> T extractClaims(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
}
