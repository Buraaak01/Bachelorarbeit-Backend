package de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Die Klasse `Ingredient` repraesentiert eine Entitaet in der Datenbanktabelle "ingredient", eine Zutat.
 * Sie enthaelt die entsprechenden Felder, die den Spalten der Tabelle entsprechen.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient {

    /**
     * Die ID der Zutat.
     */
    @Id
    String id;

    /**
     * Der Name der Zutat.
     */
    String name;

    /**
     * Die Anzahl der Kalorien pro 100 Gramm.
     */
    Double calories;

    /**
     * Die Anzahl der Proteine pro 100 Gramm.
     */
    Double proteins;

    /**
     * Die Anzahl der Fette pro 100 Gramm.
     */
    Double fats;

    /**
     * Die Anzahl der Kohlenhydrate pro 100 Gramm.
     */
    Double carbohydrates;

    /**
     * Die Liste der mit dieser Zutat verbundenen Rezept-Zutaten.
     */
    @OneToMany(mappedBy = "ingredient", orphanRemoval = true)
    List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    /**
     * Die Liste der mit dieser Zutat verbundenen Masseinheiten.
     */
    @OneToMany(mappedBy = "ingredient", orphanRemoval = true)
    List<IngredientUnit> ingredientUnits = new ArrayList<>();
}


