package com.hugh.base.service.security;

import org.junit.jupiter.api.Test;
import java.security.KeyPair;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class JwtFilterTest {

    @Test
    public void testSignAndVerify() throws Exception {
        // generate keys
        KeyPair kp = RsaKeyUtil.generate();
        RsaJwtService signer = new RsaJwtService(kp.getPrivate(), kp.getPublic());
        String token = signer.sign("user1", Map.of("roles", java.util.List.of("ROLE_USER")), 60000);
        assertNotNull(token);

        // verify using filter verifier logic (simulate parsing & verification)
        var publicKey = kp.getPublic();
        var filter = new JwtAuthenticationFilter(publicKey);
        // we cannot invoke filter easily; instead parse SignedJWT and verify
        com.nimbusds.jwt.SignedJWT jwt = com.nimbusds.jwt.SignedJWT.parse(token);
        boolean ok = jwt.verify(new com.nimbusds.jose.crypto.RSASSAVerifier((java.security.interfaces.RSAPublicKey) publicKey));
        assertTrue(ok);
    }
}
