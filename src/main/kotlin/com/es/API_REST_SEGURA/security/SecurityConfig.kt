package com.es.API_REST_SEGURA.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    private lateinit var rsaKeys: RSAKeysProperties

    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {

        return http
            .csrf { csrf -> csrf.disable() } // Cross-Site Forgery
            .authorizeHttpRequests { auth -> auth
                .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuarios/getAll").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/usuarios/{username}").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/usuarios/delete/{username}/{password}").authenticated()
                .requestMatchers(HttpMethod.PUT, "/usuarios/updatePassword").authenticated()
                .requestMatchers(HttpMethod.POST, "/usuarios/activarBono/{username}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/talleres/{id}").authenticated()
                .requestMatchers(HttpMethod.GET,"/talleres/getAll").authenticated()
                .requestMatchers(HttpMethod.POST,"/talleres/register").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/talleres//update/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/talleres/delete/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/reservas/{username}").authenticated()
                .requestMatchers(HttpMethod.POST,"/reservas/register").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/reservas/delete/{id}/taller/{tallerID}").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/reservas/delete/{tallerID}").authenticated()
                .requestMatchers(HttpMethod.GET,"/reservas/first/{username}").authenticated()
                .requestMatchers(HttpMethod.DELETE,"/reservas/deleteAll/{username}").authenticated()
                .anyRequest().permitAll()
            } // Los recursos protegidos y publicos
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic(Customizer.withDefaults())
            .build()

    }

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Método que inicializa un objeto de tipo AuthenticationManager
     */
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration) : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }


    /*
    MÉTODO PARA CODIFICAR UN JWT
     */
    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    /*
    MÉTODO PARA DECODIFICAR UN JWT
     */
    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }
}