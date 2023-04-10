package de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Diese Klasse repraesentiert die zusammengesetzte ID fuer die Verbindung zwischen einem Rezept und einer Zutat.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RecipeIngredientId implements Serializable {
    /**
     * Die ID des dazugehoerigen Rezepts.
     */
    @Column(name = "recipe_id")
    Long recipeId;

    /**
     * Die ID der dazugehoerigen Zutat.
     */
    @Column(name = "ingredient_id")
    String ingredientId;

    /**
     * Vergleicht diese RecipeIngredientId mit einem anderen Objekt auf Gleichheit.
     *
     * @param o das andere Objekt, mit dem verglichen werden soll
     * @return true, wenn die Objekte gleich sind, andernfalls false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeIngredientId recipeIngredientId = (RecipeIngredientId) o;
        return Objects.equals(recipeId, recipeIngredientId.recipeId) &&
                Objects.equals(ingredientId, recipeIngredientId.ingredientId);
    }

    /**
     * Berechnet den Hash-Code dieser RecipeIngredientId.
     *
     * @return der berechnete Hash-Code
     */
    @Override
    public int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}
