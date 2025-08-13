package com.hugh.base.service.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Generates RSA keypair for JWT signing (for demo only).
 * In production, use a secure KMS or environment-provided keys.
 */
public class RsaKeyUtil {

    public static KeyPair generate() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }

    public static String toBase64(PublicKey k) {
        return Base64.getEncoder().encodeToString(k.getEncoded());
    }

    public static String toBase64(PrivateKey k) {
        return Base64.getEncoder().encodeToString(k.getEncoded());
    }

    public static PublicKey publicFromBase64(String b64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(b64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public static PrivateKey privateFromBase64(String b64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(b64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }
}
