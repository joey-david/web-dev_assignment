package fr.univlyon1.m1if.m1if03.resas_api.util;

import fr.univlyon1.m1if.m1if03.resas_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Classe utilitaire qui fournit le token JWT, le décode, et le modifie.
 */
public class ResasJwtTokenProvider {

    private final SecretKey key = Jwts.SIG.HS512.key().build();

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(user.getLogin())
                .issuedAt(now)
                .expiration(expiryDate)
                .claim("name", user.getName())
                .signWith(key)
                .compact();
    }

    public String generateToken(Claims claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
