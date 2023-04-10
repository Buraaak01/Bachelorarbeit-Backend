package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * DTO-Klasse, die die Details eines Rezepts enthaelt. Wird fuer die Detailansicht verwendet
 */
@Data
@Builder
public class GetRecipeDetailDto {

    /**
     * Die ID des Rezepts.
     */
    private Long id;

    /**
     * Der Titel des Rezepts.
     */
    private String title;

    /**
     * Die Zubereitungsinformationen fuer das Rezept.
     */
    private String preparation;

    /**
     * Das Bild des Rezepts im Base64-Format.
     */
    private byte[] image;

    /**
     * Die Anzahl der Portionen des Rezepts.
     */
    private Double portions;

    /**
     * Ein Flag, das angibt, ob das Rezept als Favorit markiert ist.
     */
    private Boolean favorite;

    /**
     * Die Naehrwertinformationen des Rezepts.
     */
    private NutrientsDto nutrients;

    /**
     * Eine Liste der Zutaten des Rezepts.
     */
    private List<String> ingredients;
}
