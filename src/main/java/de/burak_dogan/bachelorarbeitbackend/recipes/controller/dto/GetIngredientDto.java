package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO-Klasse fuer die Darstellung von Zutateninformationen.
 */
@Data
@Builder
public class GetIngredientDto {

    /**
     * Die ID der Zutat.
     */
    private String foodId;

    /**
     * Der Name der Zutat.
     */
    private String name;

    /**
     * Die Menge der Zutat.
     */
    private Double quantity;

    /**
     * Eine Liste von Masseinheiten, die fuer die Zutat verfuegbar sind.
     */
    private List<String> units;

}
