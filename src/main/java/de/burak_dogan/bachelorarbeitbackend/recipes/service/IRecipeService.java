package de.burak_dogan.bachelorarbeitbackend.recipes.service;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.CreateRecipeDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeDetailDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeListItemDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.RecipeFilterDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Das Interface IRecipeService stellt Methoden zur Verwaltung von Rezepten bereit.
 */
public interface IRecipeService {

    /**
     * Ruft eine Liste aller Rezepte ab.
     *
     * @return Eine Liste von Rezept-Objekten.
     * @throws ServiceException Wenn ein Fehler beim Abrufen der Rezepte auftritt.
     */
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    List<GetRecipeListItemDto> getRecipes() throws ServiceException;

    /**
     * Ruft ein Rezept ab.
     *
     * @param recipeId Die ID des zu findenen Rezepts.

     * @return Das gefundene Rezept.
     * @throws ServiceException Wenn ein Fehler beim Abrufen des Rezepts auftritt oder kein Rezept mit der Rezept-ID existiert.
     */
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    GetRecipeDetailDto getSingleRecipe(Long recipeId) throws ServiceException;

    /**
     * Aktualisiert den Favoritenstatus eines Rezepts.
     *
     * @param recipeId Die ID des zu aktualisierenden Rezepts.
     * @throws ServiceException Wenn ein Fehler beim Aktualisieren des Favoritenstatus auftritt oder kein Rezept mit der Rezept-ID existiert.
     */
    @Transactional(rollbackFor = ServiceException.class)
    void updateFavoriteStatus(Long recipeId) throws ServiceException;

    /**
     * Sucht Rezepte basierend auf den Ziel-Naehrwertangaben.
     *
     * @param dto     Der Rezeptfilter mit den Ziel-Naehrstoffwerten und der Epsilon-Prozentzahl.
     * @return  Eine Liste von Rezept-Objekten, die den Naehrstoffkriterien entsprechen
     * @throws ServiceException Wenn ein Fehler beim Suchen der Rezepte auftritt.
     */
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    List<GetRecipeListItemDto> findRecipesByNutritionalValues(RecipeFilterDto dto) throws ServiceException;


    /**
     * Erstellt ein Rezept anhand der Eingaben des Users.
     *
     * @param dto                 Das Dto, das die Eingaben des Users enthaelt.
     * @throws ServiceException Wenn ein Fehler beim Suchen der Rezepte auftritt oder eine angegebene Zutat oder Masseinheit nicht existiert.
     */

    @Transactional(rollbackFor = ServiceException.class)
    void createRecipe(CreateRecipeDto dto) throws ServiceException;
}