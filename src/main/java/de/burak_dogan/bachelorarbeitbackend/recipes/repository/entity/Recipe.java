package de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse `Recipe` repraesentiert eine Entitaet in der Datenbanktabelle "recipe", ein Rezept.
 * Sie enthaelt die entsprechenden Felder, die den Spalten der Tabelle entsprechen.
 */
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    /**
     * Die eindeutige ID des Rezepts.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Der Titel des Rezepts.
     */
    String title;

    /**
     * Die Zubereitungsanleitung des Rezepts.
     */
    String preparation;

    /**
     * Das Bild des Rezepts (als Byte-Array).
     */
    byte[] image;

    /**
     * Die Portionsgroesse des Rezepts.
     */
    Double portions;

    /**
     * Der Favoritenstatus des Rezepts.
     */
    Boolean favorite;

    /**
     * Eine Liste von "RecipeIngredient"-Objekten, die die Zutaten des Rezepts darstellen.
     */
    @OneToMany(mappedBy = "recipe", orphanRemoval = true, fetch = FetchType.LAZY)
    List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}

