package me.drpuc.lda.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Jwt {
    @Value("${jwt_secret}")
    private String secret;

    @Value("${jwt_expiry}")
    private int expiry;


    public String generateToken(String callsign) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withIssuer("LDA")
                .withIssuedAt(new Date())
                .withClaim("callsign", callsign)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiry))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveCallsign(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("LDA")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("callsign").asString();
    }
}
