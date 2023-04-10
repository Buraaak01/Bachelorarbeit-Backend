package de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto;

import de.burak_dogan.bachelorarbeitbackend.recipes.validation.AtLeastOneNutritionalValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * DTO-Klasse, die die Filterkriterien (Naehrstoffe) fuer die Rezeptsuche enthaelt.
 */
@Data
@AtLeastOneNutritionalValue
public class RecipeFilterDto {
    /**
     * Die Kalorienmenge fuer die Filterung.
     */
    @Positive(message = "Calories must be a positive number!")
    Long calories;

    /**
     * Die Proteinmenge fuer die Filterung.
     */
    @Positive(message = "Proteins must be a positive number!")
    Long proteins;

    /**
     * Die Kohlenhydratmenge fuer die Filterung.
     */
    @Positive(message = "Carbohydrates must be a positive number!")
    Long carbohydrates;

    /**
     * Die Fettmenge fuer die Filterung.
     */
    @Positive(message = "Fats must be a positive number!")
    Long fats;

    /**
     * Die Abweichung der Werte fuer die Filterung.
     */
    @NotNull(message = "Please provide a deviance!")
    @Range(min = 0, max = 100, message = "Deviance must be between 0% and 100%!")
    Integer epsilon;

}
