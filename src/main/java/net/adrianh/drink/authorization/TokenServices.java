package net.adrianh.drink.authorization;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

public class TokenServices {
    
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    public static String createToken(String username) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 3);
        return Jwts.builder().setSubject(username).setExpiration(new Date(cal.getTimeInMillis())).signWith(key).compact();
    }
    
    // Validates the token and returns the subject (username)
    // Throws an exception if the signature is incorrect or if the token has expired
    public static String validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
    
}
