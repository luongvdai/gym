package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.entity.User;
import fpt.gymmanagement.utils.CommonAppUtils;
import fpt.gymmanagement.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    /*
     * 86400000 : 1000 = 86400 seconds
     * 86400 : 60 = 1440 minutes
     * 1440 : 60 = 24 hour
     * ==> 24*60*60*1000 (1 day)
     * */
    private static final long JWT_EXPIRATION = 86400000;
    private static final long REFRESH_EXPIRATION = 604800000;
    private static final long ONE_YEAR_EXPIRED = 31536000000L;//dung de test

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);//get username 1h3p30s video https://www.youtube.com/watch?v=KxqlJblhzfI
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //túm gọn 3 hàm trên sẽ như này
//    public String getUsernameFromToken(String token){
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody().getSubject();
//    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User userDetails) {
        return buildToken(CommonAppUtils.buildRoleClaims(userDetails),
                userDetails, JWT_EXPIRATION);
    }

    private String buildToken(Map<String, String> extraClaims, User userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(User userDetails) {
        return buildToken(CommonAppUtils.buildRoleClaims(userDetails),
                userDetails, REFRESH_EXPIRATION);
    }

    @Override
    public Boolean isTokenValid(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
