package com.adoptasalvavidas.adoptasalvavidasBack.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${security.jwt.key.private}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;


//    public String createToken(Authentication authentication) {
//        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
//        System.out.println("Clave privada cargada:"+privateKey);
//
//
//        String username = ((User) authentication.getPrincipal()).getUsername();
//
//
//        String authorities = authentication.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        String jwtToken = JWT.create()
//                .withIssuer(this.userGenerator)
//                .withSubject(username)  // Asignar solo el nombre de usuario aquí
//                .withClaim("authorities", authorities)
//                .withIssuedAt(new Date())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))  // Expira en 30 minutos
//                .withJWTId(UUID.randomUUID().toString())
//                .withNotBefore(new Date(System.currentTimeMillis()))
//                .sign(algorithm);
//        return jwtToken;
//    }
        public String createToken(Authentication authentication) {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            System.out.println("Clave privada cargada:" + privateKey);

            // Usar el nombre de usuario directamente
            String username = ((User) authentication.getPrincipal()).getUsername();

            // Modificar la asignación de authorities para agregar el prefijo ROLE_
            String authorities = authentication.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> "ROLE_" + grantedAuthority.getAuthority())  // Agregar el prefijo ROLE_
                    .collect(Collectors.joining(","));

            String jwtToken = JWT.create()
                    .withIssuer(this.userGenerator)
                    .withSubject(username)  // Asignar solo el nombre de usuario aquí
                    .withClaim("authorities", authorities)  // Incluir las authorities con el prefijo ROLE_
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))  // Expira en 30 minutos
                    .withJWTId(UUID.randomUUID().toString())
                    .withNotBefore(new Date(System.currentTimeMillis()))
                    .sign(algorithm);
            return jwtToken;
        }



    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Token invalid, not Authorized");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        Claim claim = decodedJWT.getClaim(claimName);
        if (claim.isNull()) {
            throw new IllegalArgumentException("Claim not found: " + claimName);
        }
        return claim;
    }

    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return  decodedJWT.getClaims();
    }
}
