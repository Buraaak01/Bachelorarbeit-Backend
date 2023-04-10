package de.burak_dogan.bachelorarbeitbackend.recipes.config;

import de.burak_dogan.bachelorarbeitbackend.recipes.config.properties.CorsProperties;
import de.burak_dogan.bachelorarbeitbackend.recipes.config.properties.RequestProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

/**
 * Konfigurationsklasse fuer die Anwendung.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
        RequestProperties.class,
        CorsProperties.class
})
public class AppConfig {

    /**
     * Erzeugt eine Instanz von RestTemplate, die fuer HTTP-Anfragen verwendet wird.
     *
     * @return die {@link RestTemplate}-Instanz
     */
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Erzeugt eine Instanz von ModelMapper, die zur Objektzuordnung verwendet wird.
     *
     * @return die {@link ModelMapper}-Objekt-Instanz
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
