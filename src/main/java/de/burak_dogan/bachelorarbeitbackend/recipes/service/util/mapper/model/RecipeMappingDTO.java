package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO-Klasse fuer die Abbildung eines Rezepts.
 * Die Daten werden aus der Edamam-API-Request entnommen.
 */
@Data
@NoArgsConstructor
@Builder
public class RecipeMappingDTO {

    /**
     * Der Titel des Rezepts.
     */
    private String label;

    /**
     * Der Link zur Zubereitung des Rezepts.
     */
    private String url;

    /**
     * Der Bildpfad des Rezepts als URL.
     */
    private String image;

    /**
     * Die Anzahl der Portionen des Rezepts.
     */
    private Double yield;

    /**
     * Die Liste der Zutaten des Rezepts.
     */
    @JsonProperty(value = "ingredients")
    private List<RecipeIngredientMappingDTO> recipeIngredientMappingDTOList;

    /**
     * Konstruktor fuer die Erstellung eines RecipeMappingDTO-Objekts.
     * Dadurch wird sichergestellt, dass diese Werte beim Mappen gesetzt werden.
     *
     * @param label                        Der Titel des Rezepts.
     * @param url                          Der Link zur Zubereitung des Rezepts.
     * @param image                        Der Bildpfad des Rezepts als URL.
     * @param yield                        Die Anzahl der Portionen des Rezepts.
     * @param recipeIngredientMappingDTOList Die Liste der Zutaten des Rezepts.
     */
    @JsonCreator
    public RecipeMappingDTO(@JsonProperty(required = true) String label,
                            @JsonProperty(required = true) String url,
                            @JsonProperty(required = true) String image,
                            @JsonProperty(required = true) Double yield,
                            @JsonProperty(required = true) List<RecipeIngredientMappingDTO> recipeIngredientMappingDTOList) {
        this.label = label;
        this.url = url;
        this.image = image;
        this.yield = yield;
        this.recipeIngredientMappingDTOList = recipeIngredientMappingDTOList;
    }


}
