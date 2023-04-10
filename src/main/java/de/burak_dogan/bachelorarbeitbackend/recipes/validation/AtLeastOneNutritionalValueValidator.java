package de.burak_dogan.bachelorarbeitbackend.recipes.validation;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.RecipeFilterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validierungsklasse fuer die Annotation AtLeastOneNutritionalValue.
 * Ueberprueft, ob mindestens ein Naehrwertparameter in einem RecipeFilterDto angegeben ist.
 */
public class AtLeastOneNutritionalValueValidator implements ConstraintValidator<AtLeastOneNutritionalValue, RecipeFilterDto> {

    /**
     * Ueberprüft, ob mindestens ein Naehrwertparameter angegeben ist.
     *
     * @param dto      Das RecipeFilterDto-Objekt, das validiert wird
     * @param context  Der ConstraintValidatorContext
     * @return true, wenn mindestens ein Naehrwertparameter angegeben ist, sonst false
     */
    @Override
    public boolean isValid(RecipeFilterDto dto, ConstraintValidatorContext context) {
        return dto.getCalories() != null || dto.getProteins() != null || dto.getCarbohydrates() != null || dto.getFats() != null;
    }
}
