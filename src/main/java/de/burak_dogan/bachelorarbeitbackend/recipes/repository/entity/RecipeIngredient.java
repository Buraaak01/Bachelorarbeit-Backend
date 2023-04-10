package de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity;


import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.embeddable.RecipeIngredientId;
import jakarta.persistence.*;
import lombok.*;

/**
 * Diese Klasse repraesentiert eine Zutat eines Rezepts in der Datenbank.
 * Sie enthaelt Attribute wie die Menge, den Titel und die Verbindung zu einem bestimmten Rezept und einer bestimmten Zutat.
 * Die Verbindung wird durch die Kombination von "recipeId" und "ingredientId" gebildet.
 */
@ToString
@Getter
@Setter
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    /**
     * Die eindeutige ID der Kombination aus Rezept und Zutat.
     */
    @EmbeddedId
    @Setter(AccessLevel.NONE)
    RecipeIngredientId id = new RecipeIngredientId();

    /**
     * Das Rezept, zu dem die Zutat gehoert.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    Recipe recipe;

    /**
     * Die Zutat, die zu einem Rezept gehoert.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId")
    Ingredient ingredient;

    /**
     * Die Menge der Zutat im Rezept.
     */
    Double quantity;

    /**
     * Der Titel der Zutat im Rezept.
     */
    String title;
}


