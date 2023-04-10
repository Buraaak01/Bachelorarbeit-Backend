package de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Die Klasse `IngredientUnit` repraesentiert eine Entitaet in der Datenbanktabelle "ingredient_unit".
 * Sie enthaelt die entsprechenden Felder, die den Spalten der Tabelle entsprechen.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_unit")
public class IngredientUnit {

    /**
     * Die ID der Ingredient-Einheit.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Das Ingredient, zu dem diese Einheit gehoert.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient;

    /**
     * Das Label der Ingredient-Einheit.
     */
    String label;

    /**
     * Der Wert der Ingredient-Einheit in Gramm.
     */
    Double value;
}
