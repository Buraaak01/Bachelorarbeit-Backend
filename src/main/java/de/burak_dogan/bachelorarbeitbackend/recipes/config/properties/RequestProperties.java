package de.burak_dogan.bachelorarbeitbackend.recipes.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Konfigurationsklasse fuer die Eigenschaften der Anfragen an die Edamam APIs.
 */
@Data
@ConfigurationProperties(prefix = "app.request")
public class RequestProperties {

    /**
     * Die App-ID fuer die Rezeptanfragen an die Edamam API.
     */
    private String recipeAppId;

    /**
     * Der App-Schluessel fuer die Rezeptanfragen an die Edamam API.
     */
    private String recipeAppKey;

    /**
     * Die App-ID fuer die Zutatenanfragen an die Edamam API.
     */
    private String ingredientAppId;

    /**
     * Der App-Schluessel fuer die Zutatenanfragen an die Edamam API.
     */
    private String ingredientAppKey;
}
