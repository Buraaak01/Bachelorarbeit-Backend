package de.burak_dogan.bachelorarbeitbackend.recipes.config;

import de.burak_dogan.bachelorarbeitbackend.recipes.config.properties.CorsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Konfigurationsklasse fuer die Cross-Origin Resource Sharing (CORS).
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    /**
     * Enthaelt die entsprechenden Eigenschaften fuer die Konfiguration.
     */
    private final CorsProperties corsProperties;

    /**
     * Konstruktor fuer die CorsConfiguration-Klasse.
     *
     * @param corsProperties die CORS-Eigenschaften
     */

    @Autowired
    public CorsConfiguration(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    /**
     * Konfiguriert die CORS-Einstellungen fuer die Anwendung.
     *
     * @param registry das CorsRegistry-Objekt zum Hinzufuegen von CORS-Mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins())
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
