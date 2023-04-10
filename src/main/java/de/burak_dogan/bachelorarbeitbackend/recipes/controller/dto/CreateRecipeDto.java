package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * DTO-Klasse, die die Informationen fuer die Erstellung eines Rezepts enthaelt.
 */
@Data
public class CreateRecipeDto {

    /**
     * Der Titel des Rezepts.
     */
    @NotBlank(message = "Please provide a recipe title!")
    private String title;

    /**
     * Die Anzahl der Portionen des Rezepts.
     */
    @NotNull(message = "Please provide the amount of portions!")
    private Double portions;


    /**
     * Die Liste der Zutaten, inklusive Mengen und verwendete Einheiten fuer das Rezept.
     */
    @NotEmpty(message = "Please provide a list of ingredients!")
    @Valid
    private List<RecipeIngredientDto> ingredients;

    /**
     * Die Zubereitungsinformationen fuer das Rezept.
     */
    @NotBlank(message = "Please provide information about the preparation!")
    private String preparation;

    /**
     * Das Bild des Rezepts im Base64-Format.
     */
    @NotNull(message = "Please provide an image in base64!")
    private byte[] image;
}
