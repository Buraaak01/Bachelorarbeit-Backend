package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO-Klasse, die Informationen ueber die Naehrwerte einer Zutat enthaelt.
 */
@Data
@Builder
public class NutrientsDto {

    /**
     * Die Kalorienmenge.
     */
    private Long calories;

    /**
     * Die Menge an Proteinen.
     */
    private Long proteins;

    /**
     * Die Menge an Kohlenhydraten.
     */
    private Long carbohydrates;

    /**
     * Die Menge an Fetten.
     */
    private Long fats;

}