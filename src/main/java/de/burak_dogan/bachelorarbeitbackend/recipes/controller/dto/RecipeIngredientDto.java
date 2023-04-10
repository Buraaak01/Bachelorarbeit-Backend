package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



/**
 * DTO-Klasse, die die Informationen fuer eine Zutat in einem Rezept enthaelt.
 */
@Data
public class RecipeIngredientDto {

    /**
     * Die eindeutige Identifikationsnummer der Zutat.
     */
    @NotBlank(message = "Please provide a foodId!")
    private String foodId;

    /**
     * Der Name der Zutat.
     */
    @NotBlank(message = "Please provide an ingredient name!")
    private String name;

    /**
     * Die Menge der Zutat im Rezept.
     */
    @NotNull(message = "Please provide a quantity!")
    private Double quantity;

    /**
     * Die Masseinheit der Zutat.
     */
    @NotBlank(message = "Please provide a valid measuring unit!")
    private String unit;

}
