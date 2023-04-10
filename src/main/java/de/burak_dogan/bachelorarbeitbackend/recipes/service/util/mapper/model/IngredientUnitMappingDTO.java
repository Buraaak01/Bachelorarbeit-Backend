package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO-Klasse fuer die Abbildung von allen moeglichen Masseinheiten, die es fuer eine Zutat gibt.
 * Die Daten werden aus der Edamam-API-Request entnommen.
 */
@Getter
@Setter
@Builder
public class IngredientUnitMappingDTO {
    /**
     * Der Name der Masseinheit, z.B. "Cup".
     */
    private String label;

    /**
     * Das Gewicht der Masseinheit in Gramm.
     */
    private Double weight;
}
