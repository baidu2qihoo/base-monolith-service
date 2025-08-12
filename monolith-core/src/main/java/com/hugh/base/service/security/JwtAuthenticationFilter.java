package com.hugh.base.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.JOSEException;

/**
 * JwtAuthenticationFilter now verifies RSA signature using Nimbus JOSE + JWT.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final PublicKey publicKey;

    public JwtAuthenticationFilter(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                SignedJWT jwt = SignedJWT.parse(token);
                // verify signature
                RSASSAVerifier verifier = new RSASSAVerifier((java.security.interfaces.RSAPublicKey) publicKey);
                boolean ok = jwt.verify(verifier);
                if (ok) {
                    var claims = jwt.getJWTClaimsSet();
                    String sub = claims.getSubject();
                    List<String> roles = (List<String>) claims.getClaim("roles"); // expect roles claim as array
                    var authorities = roles == null ? List.of() : roles.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
                    var authn = new UsernamePasswordAuthenticationToken(sub, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authn);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } catch (ParseException | JOSEException e) {
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
