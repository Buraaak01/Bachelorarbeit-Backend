package de.burak_dogan.bachelorarbeitbackend.recipes.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Klasse zur Konfiguration der Eigenschaften fuer Cross-Origin Resource Sharing (CORS).
 */
@Data
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /**
     * Die erlaubten Urspruenge (Origins) fuer CORS-Anfragen.
     */
    private String allowedOrigins;
}
