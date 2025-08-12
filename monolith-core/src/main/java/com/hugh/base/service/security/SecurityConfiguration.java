package com.hugh.base.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Base64;

/**
 * Spring Security config wiring JwtAuthenticationFilter.
 */
@Configuration
public class SecurityConfiguration {

    // In a production system load the public key from JWKS or KMS. For demo we support environment variable.
    @Bean
    public PublicKey jwtPublicKey() throws Exception {
        String b64 = System.getenv("JWT_PUBLIC_KEY_B64"); // base64 encoded X.509
        if (b64 == null || b64.isBlank()) return null;
        return RsaKeyUtil.publicFromBase64(b64);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PublicKey jwtPublicKey) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**", "/api/config/**", "/api/registry/**", "/api/metrics/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        if (jwtPublicKey != null) {
            http.addFilterBefore(new JwtAuthenticationFilter(jwtPublicKey), UsernamePasswordAuthenticationFilter.class);
        }
        return http.build();
    }
}
