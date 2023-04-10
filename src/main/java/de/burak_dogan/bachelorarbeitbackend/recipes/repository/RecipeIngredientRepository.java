package de.burak_dogan.bachelorarbeitbackend.recipes.repository;

import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Das Repository fuer die Entitaet {@link RecipeIngredient}, das Datenbankzugriffe fuer RecipeIngredient-Objekte ermoeglicht.
 * Ein Eintrag entspricht einer Zutat im Rezept.
 */
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

}
