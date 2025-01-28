package translation_api.translation.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
@Component
public class JWTUtil {
    @Value("${jwt.token.private}")
    private String privateKey;
    @Value("${jwt.token.user}")
    private String userGenerator;


    public String createToken(Authentication authentication){


        Algorithm algoritmo=Algorithm.HMAC256(privateKey);

        String username= authentication.getPrincipal().toString();
        String jwtGenerator= JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis()+1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algoritmo);
        return jwtGenerator;

    }
    public DecodedJWT validateToken(String token){
        Algorithm algoritmo=Algorithm.HMAC256(privateKey);
        JWTVerifier verifier=JWT.require(algoritmo).withIssuer(this.userGenerator).build();

        DecodedJWT decode=verifier.verify(token);

        return decode;
    }

    public String extractUserName(DecodedJWT decode){
        return decode.getSubject();
    }
}
