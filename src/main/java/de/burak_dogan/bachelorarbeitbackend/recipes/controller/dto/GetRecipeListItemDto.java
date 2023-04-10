package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO-Klasse, die die Details eines Rezepts enthaelt. Wird fuer die Listenansicht verwendet
 */
@Data
@Builder
public class GetRecipeListItemDto {

    /**
     * Die ID des Rezepts.
     */
    private Long id;

    /**
     * Der Titel des Rezepts.
     */
    private String title;

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


}
