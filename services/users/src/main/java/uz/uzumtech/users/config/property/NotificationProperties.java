package uz.uzumtech.users.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.client.notifications")
public class NotificationProperties {

    private String user;

    private String password;

    private String url;

    private String name;
}
