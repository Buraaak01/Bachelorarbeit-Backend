package de.burak_dogan.bachelorarbeitbackend.recipes.repository;

import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Das Repository fuer die Entitaet {@link Recipe}, das Datenbankzugriffe fuer Recipe-Objekte ermoeglicht.
 * Ein Eintrag entspricht einem Rezept.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Sucht ein Rezept anhand seines Titels.
     *
     * @param title Der Titel des Rezepts.
     * @return Ein Optional, das entweder das gefundene Rezept enthaelt oder leer ist.
     */
    Optional<Recipe> findByTitle(String title);

}
