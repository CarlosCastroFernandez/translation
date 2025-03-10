package translation_api.translation.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
@Component
public class JWTUtil {
    @Value("${jwt.token.private}")
    private String privateKey;
    @Value("${jwt.token.user}")
    private String userGenerator;


    public RSAPrivateKey rsaPrivate(String ruta){
        Path path= Paths.get(ruta);
        try (InputStream is=JWTUtil.class.getClassLoader().getResourceAsStream(ruta)){
            byte[]lecturaByte=is.readAllBytes();
            String datos=new String(lecturaByte);
            datos =datos.replace("-----BEGIN PRIVATE KEY-----","");
            datos =datos.replace("-----END PRIVATE KEY-----","");
            datos=datos.replaceAll("\\s","");
            lecturaByte= Base64.getDecoder().decode(datos);
            PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(lecturaByte);
            KeyFactory keyFActory=KeyFactory.getInstance("RSA");
            RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) keyFActory.generatePrivate(keySpec);
            return rsaPrivateKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }
    public RSAPublicKey rsaPublicKey(String ruta){
        Path path= Paths.get(ruta);
        try (InputStream is=JWTUtil.class.getClassLoader().getResourceAsStream(ruta)){
            byte[]lecturaByte=is.readAllBytes();
            String datos=new String(lecturaByte);
            datos =datos.replace("-----BEGIN PUBLIC KEY-----","");
            datos =datos.replace("-----END PUBLIC KEY-----","");
            datos=datos.replaceAll("\\s","");
            lecturaByte= Base64.getDecoder().decode(datos);
            X509EncodedKeySpec keySpec=new X509EncodedKeySpec(lecturaByte);
            KeyFactory keyFActory=KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPublicKey=(RSAPublicKey) keyFActory.generatePublic(keySpec);
            return rsaPublicKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }


    public String createToken(Authentication authentication){


        Algorithm algoritmo=Algorithm.RSA256(null,rsaPrivate("private_key.pem"));
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
        DecodedJWT decode=null;
        try{
            Algorithm algoritmo=Algorithm.RSA256(rsaPublicKey("public_key.pem"),null);
            JWTVerifier verifier=JWT.require(algoritmo).withIssuer(this.userGenerator).build();

             decode=verifier.verify(token);

        }catch (JWTVerificationException e){
            throw new JWTVerificationException("El token ha expirado");

        }
        return decode;

    }

    public String extractUserName(DecodedJWT decode){
        return decode.getSubject();
    }
}
