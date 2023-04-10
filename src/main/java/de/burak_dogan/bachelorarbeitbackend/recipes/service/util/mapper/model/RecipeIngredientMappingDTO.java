package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.InvalidMeasureException;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.InvalidMeasures;
import lombok.Data;
import org.apache.commons.lang3.EnumUtils;


/**
 * DTO-Klasse fuer die Abbildung von einem Zutatenelement in einer Rezeptzuordnung.
 * Die Daten werden aus der Edamam-API-Request entnommen.
 */
@Data
public class RecipeIngredientMappingDTO {

    /**
     * Die ID der Zutat.
     */
    private String foodId;

    /**
     * Der Text der Zutat.
     */
    private String text;

    /**
     * Die Menge der Zutat.
     */
    private Double quantity;

    /**
     * Die Masseinheit der Zutat, z.B. "Cup".
     */
    private String measure;

    /**
     * Die zum Rezept zugehoerige Zutat als IngredientMappingDTO-Objekt.
     * Wird beim Object-Mapping ignoriert und erst gesetzt, wenn alle anderen Werte des RecipeIngredientMappingDTOs gesetzt wurden.
     */
    @JsonIgnore
    private IngredientMappingDTO ingredientMappingDTO;

    /**
     * Konstruktor fuer die Erstellung eines RecipeIngredientMappingDTO-Objekts.
     * Dadurch wird sichergestellt, dass diese Werte beim Mappen gesetzt werden.
     * @param foodId   Die ID der Zutat.
     * @param text     Der Text der Zutat.
     * @param quantity Die Menge der Zutat.
     * @param measure  Das Maß der Zutat.
     * @throws InvalidMeasureException Wird geworfen, wenn die Masseinheit ungueltig ist.
     */
    @JsonCreator
    public RecipeIngredientMappingDTO(@JsonProperty(required = true) String foodId,
                                      @JsonProperty(required = true) String text,
                                      @JsonProperty(required = true) Double quantity,
                                      @JsonProperty(required = true) String measure) throws InvalidMeasureException {
        this.foodId = foodId;
        this.text = text;
        this.quantity = quantity;
        this.measure = measure;
        validateMeasure();
    }

    /**
     * Ueberprüft, ob die Masseinheit der Zutat im Rezept gültig ist.
     *
     * @throws InvalidMeasureException Wird geworfen, wenn die Masseinheit ungültig ist.
     */
    protected void validateMeasure() throws InvalidMeasureException {
        if (EnumUtils.isValidEnum(InvalidMeasures.class, measure.toUpperCase())) {
            throw new InvalidMeasureException("The measure " + measure + " is invalid.");
        }
    }
}
