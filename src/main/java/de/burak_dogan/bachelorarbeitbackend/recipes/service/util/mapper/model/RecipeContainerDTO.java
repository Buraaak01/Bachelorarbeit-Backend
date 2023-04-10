package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO-Klasse fuer die Rezept-Containerinformationen. Hier werden alle Rezepte eines Request gespeichert.
 * Des Weiteren wird hier auch der Link zur naechsten Request gespeichert, falls vorhanden.
 * Die Daten werden aus der Edamam-API-Request entnommen.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeContainerDTO {
    /**
     * Die Liste der Rezept-Mapping-DTOs, die aus der Response entnommenen Rezepte.
     */
    private List<RecipeMappingDTO> recipeMappingDTOList;

    /**
     * Der Link des Rezept-Containers, der dem Link zum naechsten Request entspricht, falls vorhanden.
     */
    private String link;

    /**
     * Setzt die Liste von Rezept-Mapping-DTOs.
     *
     * @param recipeMappingDTOList die Liste von Rezept-Mapping-DTOs
     * @return das aktualisierte RecipeContainerDTO-Objekt
     */
    public RecipeContainerDTO with(List<RecipeMappingDTO> recipeMappingDTOList) {
        this.recipeMappingDTOList = recipeMappingDTOList;
        return this;
    }

    /**
     * Setzt den Link des Rezept-Containers.
     *
     * @param link der Link des Rezept-Containers
     * @return das aktualisierte RecipeContainerDTO-Objekt
     */
    public RecipeContainerDTO with(String link) {
        this.link = link;
        return this;
    }

    /**
     * Gibt eine Zeichenfolge zurueck, die enthaltenen Rezepte im Modell darstellt.
     *
     * @return Eine Zeichenfolge, die die enthaltenen Rezepte im Modell darstellt.
     */
    @Override
    public String toString() {
        return "Recipes in Model: " + recipeMappingDTOList.stream().map(RecipeMappingDTO::getLabel).toList();
    }
}
