package com.hugh.base.service.security;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

/**
 * Simple CLI to create JWT using RsaJwtService. Requires nimbus-jose-jwt on classpath.
 * Usage: java -cp target/monolith-core-1.0.0.jar com.hugh.base.service.security.JwtCli private.pem sub ttlMs
 */
public class JwtCli {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("usage: JwtCli <privatePemPath> <subject> <ttlMs>"); return;
        }
        String privatePem = new String(Files.readAllBytes(Paths.get(args[0])));
        // Note: RsaKeyUtil.privateFromBase64 expects base64 PKCS8, so convert externally if needed.
        System.out.println("JwtCli is a helper. Use proper key conversion for production.");
    }
}
