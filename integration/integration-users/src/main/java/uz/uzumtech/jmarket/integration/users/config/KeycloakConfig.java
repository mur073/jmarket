package uz.uzumtech.jmarket.integration.users.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.uzumtech.jmarket.integration.users.config.property.KeycloakProperties;

@Configuration
@RequiredArgsConstructor
public class KeycloakConfig {

    private final KeycloakProperties kcProps;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(kcProps.getServerUrl())
                .realm(kcProps.getRealm())
                .clientId(kcProps.getClientId())
                .clientSecret(kcProps.getClientSecret())
                .grantType("client_credentials")
                .build();
    }
}
