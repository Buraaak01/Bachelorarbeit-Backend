package de.burak_dogan.bachelorarbeitbackend.recipes.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation zur Validierung, dass mindestens ein Naehrwertparameter angegeben wird.
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNutritionalValueValidator.class)
public @interface AtLeastOneNutritionalValue {
//Die Klasse wurde mit Hilfe der offiziellen Dokumentation erstellt: https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html

    /**
     * Gibt die Fehlermeldung an, wenn die Validierung fehlschlaegt.
     *
     * @return Die Fehlermeldung
     */
    String message() default "Bitte geben Sie mindestens einen Nährwertparameter an!";

    /**
     * Gibt die Validierungsgruppen an.
     *
     * @return Die Validierungsgruppen
     */
    Class<?>[] groups() default {};

    /**
     * Gibt das Nutzlastobjekt an, das mit der Constraint verknuepft ist.
     *
     * @return Die Nutzlast
     */
    Class<? extends Payload>[] payload() default {};
}
