package de.burak_dogan.bachelorarbeitbackend.recipes.repository;

import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.IngredientUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Das Repository fuer die Entitaet {@link IngredientUnit}, das Datenbankzugriffe fuer IngredientUnit-Objekte ermoeglicht.
 * Ein Eintrag entspricht einer moeglichen Einheit, die der Nutzer fuer eine Zutat beim Erstellen eines Rezepts angeben kann.
 */
@Repository
public interface IngredientUnitRepository extends JpaRepository<IngredientUnit, Long> {

}
