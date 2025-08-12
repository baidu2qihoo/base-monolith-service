package com.hugh.base.service.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * Minimal RSA-signed JWT builder using Nimbus JOSE + JWT (you must add dependency if using).
 * This is a demo implementation skeleton.
 */
public class RsaJwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RsaJwtService(PrivateKey priv, PublicKey pub) {
        this.privateKey = priv;
        this.publicKey = pub;
    }

    public String sign(String subject, Map<String, Object> claims, long ttlMs) throws JOSEException {
        Instant now = Instant.now();
        JWTClaimsSet.Builder cb = new JWTClaimsSet.Builder();
        cb.subject(subject);
        cb.issueTime(Date.from(now));
        cb.expirationTime(Date.from(now.plusMillis(ttlMs)));
        claims.forEach(cb::claim);
        SignedJWT jwt = new SignedJWT(new com.nimbusds.jose.JWSHeader(JWSAlgorithm.RS256), cb.build());
        RSASSASigner signer = new RSASSASigner(privateKey);
        jwt.sign(signer);
        return jwt.serialize();
    }
}
