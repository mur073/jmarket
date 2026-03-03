package uz.uzumtech.jmarket.integration.users.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String realm;

    private String clientId;

    private String clientSecret;

    private String serverUrl;

    private String tokenEndpoint;
}
