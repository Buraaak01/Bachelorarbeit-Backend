package de.burak_dogan.bachelorarbeitbackend.recipes.service;


import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetIngredientDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Das Interface IIngredientService stellt Methoden zur Verwaltung von Zutaten und ihren Naehrwerten bereit.
 */
public interface IIngredientService {


    /**
     * Ruft eine Liste aller Rezepte ab.
     *
     * @return Eine Liste von Rezept-Objekten.
     * @throws ServiceException Wenn ein Fehler beim Abrufen der Rezepte auftritt.
     */
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    List<GetIngredientDto> getIngredients() throws ServiceException;
}
