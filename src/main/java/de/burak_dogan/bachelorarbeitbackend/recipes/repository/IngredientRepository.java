package de.burak_dogan.bachelorarbeitbackend.recipes.repository;

import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Das Repository fuer die Entitaet {@link Ingredient}, das Datenbankzugriffe fuer Ingredient-Objekte ermoeglicht.
 * Ein Eintrag entspricht einer Zutat mit ihren Naehrstoffen.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Ermittelt alle IDs der Ingredients.
     *
     * @return Eine Liste aller Ingredient-IDs.
     */
    @Query("SELECT i.id FROM Ingredient i")
    List<String> findAllIds();

    /**
     * Sucht ein Ingredient anhand seiner ID.
     *
     * @param id Die ID des Ingredients.
     * @return Ein Optional, das entweder die gefundene {@link Ingredient}-Entity enthaelt oder leer ist.
     */
    Optional<Ingredient> findById(String id);

}
