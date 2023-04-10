package de.burak_dogan.bachelorarbeitbackend.recipes.service;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetIngredientDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.IngredientRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.IngredientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Eine Implementierung des Interface 'IIngredientService', welche Methoden zur Verwaltung von Zutaten und ihren Naehrwerten bereitstellt.
 */
@Service
public class IngredientService implements IIngredientService {

    /**
     * Das IngredientRepository-Objekt fuer den Zugriff auf Zutat-Daten
     */
    private final IngredientRepository ingredientRepository;

    /**
     * Der IngredientMapper fuer die Konvertierung von Zutat-Objekten zu DTOs
     */
    private final IngredientMapper ingredientMapper;


    /**
     * Konstruktor der Klasse RecipeService.
     *
     * @param ingredientRepository        Das IngredientRepository-Objekt fuer den Zugriff auf Zutat-Daten
     * @param ingredientMapper            Der IngredientMapper fuer die Konvertierung von Zutat-Objekten zu DTOs
     */
    @Autowired
    public IngredientService(IngredientRepository ingredientRepository, IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<GetIngredientDto> getIngredients() throws ServiceException {

        try {
            return ingredientRepository.findAll().stream().map(ingredientMapper::mapModelToDto).toList();
        } catch (Exception e) {
            throw new ServiceException("Ingredients could not be loaded!", e);
        }
    }
}
