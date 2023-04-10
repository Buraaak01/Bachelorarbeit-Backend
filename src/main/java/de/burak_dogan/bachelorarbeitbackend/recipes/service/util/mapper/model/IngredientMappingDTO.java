package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.JsonKeyNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO-Klasse fuer die Abbildung von Zutaten.
 * Die Daten werden aus der Edamam-API-Request entnommen.
 */
@Data
@NoArgsConstructor
public class IngredientMappingDTO {
    /**
     * Die eindeutige ID der Zutat.
     */
    private String foodId;

    /**
     * Das Label der Zutat.
     */
    private String label;

    /**
     * Der Kaloriengehalt der Zutat.
     */
    private Double calories;

    /**
     * Der Proteingehalt der Zutat.
     */
    private Double proteins;

    /**
     * Der Fettgehalt der Zutat.
     */
    private Double fats;

    /**
     * Der Kohlenhydratgehalt der Zutat.
     */
    private Double carbohydrates;

    /**
     * Die Gramm-Menge einer Einheit der Zutat.
     * Wird beim Object-Mapping ignoriert und erst bei der Abfrage aller moeglichen Masseinheiten des Rezepts gesetzt.
     */
    @JsonIgnore
    private Double unitInGrams;

    /**
     * Die Liste aller moeglichen Masseinheiten der Zutat.
     */
    private List<IngredientUnitMappingDTO> ingredientUnitMappingDTOS;

    /**
     * Setzt die Makronaehrstoffwerte aus der "nutrients" JSON-Eigenschaft.
     *
     * @param nutrients Die Map von Naehrstoffnamen zu Werten.
     * @throws JsonKeyNotFoundException Wenn einer der erforderlichen Naehrstoffschluessel fehlt.
     */
    @JsonProperty(value = "nutrients")
    public void setMacros(Map<String, Double> nutrients) throws JsonKeyNotFoundException {
        String proteinsKey = "PROCNT";
        String fatsKey = "FAT";
        String carbohydratesKey = "CHOCDF";
        String caloriesKey = "ENERC_KCAL";

        if (!nutrients.containsKey(proteinsKey) || !nutrients.containsKey(fatsKey) ||
                !nutrients.containsKey(carbohydratesKey) || !nutrients.containsKey(caloriesKey)) {
            throw new JsonKeyNotFoundException("Response is invalid! Nutrients information is not provided!");
        }

        proteins = nutrients.get(proteinsKey);
        fats = nutrients.get(fatsKey);
        carbohydrates = nutrients.get(carbohydratesKey);
        calories = nutrients.get(caloriesKey);
    }

    /**
     * Konstruiert ein IngredientMappingDTO-Objekt mit dem angegebenen Label und der angegebenen foodID.
     * Dadurch wird sichergestellt, dass diese Werte beim Mappen gesetzt werden.
     *
     * @param label  Das Label der Zutat.
     * @param foodId Die ID der Zutat.
     */
    @JsonCreator
    public IngredientMappingDTO(@JsonProperty(required = true) String label,
                                @JsonProperty(required = true) String foodId) {
        this.foodId = foodId;
        this.label = label;
    }
}
