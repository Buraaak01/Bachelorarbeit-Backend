package de.burak_dogan.bachelorarbeitbackend.recipes.service;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.*;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.DuplicateIngredientException;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.EntityNotFoundException;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.IngredientRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.RecipeIngredientRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.RecipeRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Ingredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.IngredientUnit;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Recipe;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.RecipeIngredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.RecipeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Eine Implementierung des Interface 'IRecipeService', welche Methoden zur Verwaltung von Rezepten bereitstellt.
 */

@Slf4j
@Service
public class RecipeService implements IRecipeService {

    /**
     * Das RecipeRepository-Objekt fuer den Zugriff auf Rezept-daten
     */
    private final RecipeIngredientRepository recipeIngredientRepository;

    /**
     * Das IngredientRepository-Objekt fuer den Zugriff auf Zutat-Daten
     */
    private final RecipeRepository recipeRepository;

    /**
     * Das RecipeIngredientRepository-Objekt fuer den Zugriff auf Rezept-Zutaten-Beziehungen
     */
    private final IngredientRepository ingredientRepository;

    /**
     * Der RecipeMapper fuer die Konvertierung von Rezept-Objekten zu DTOs
     */
    private final RecipeMapper recipeMapper;


    /**
     * Konstruktor der Klasse RecipeService.
     *
     * @param recipeRepository           Das RecipeRepository-Objekt fuer den Zugriff auf Rezept-daten
     * @param ingredientRepository       Das IngredientRepository-Objekt fuer den Zugriff auf Zutat-Daten
     * @param recipeIngredientRepository Das RecipeIngredientRepository-Objekt fuer den Zugriff auf Rezept-Zutaten-Beziehungen
     * @param recipeMapper               Der RecipeMapper fuer die Konvertierung von Rezept-Objekten zu DTOs
     */
    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, RecipeMapper recipeMapper
    ) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeMapper = recipeMapper;
    }


    @Override
    public List<GetRecipeListItemDto> getRecipes() throws ServiceException {
        List<Recipe> recipes;
        try {
            recipes = recipeRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Recipes could not be loaded!", e);
        }

        List<GetRecipeListItemDto> getRecipeListItemDtoList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            NutrientsDto nutrientsDto = getTotalNutrients(recipe);
            GetRecipeListItemDto getRecipeListItemDto = recipeMapper.mapModelToListItemDto(recipe, nutrientsDto);
            getRecipeListItemDtoList.add(getRecipeListItemDto);
        }
        return getRecipeListItemDtoList;
    }

    @Override
    public GetRecipeDetailDto getSingleRecipe(Long recipeId) throws ServiceException {
        Optional<Recipe> optionalRecipe;
        try {
            optionalRecipe = recipeRepository.findById(recipeId);
        } catch (Exception e) {
            throw new ServiceException("The Recipe could not be loaded!", e);
        }

        if (optionalRecipe.isEmpty()) {
            throw new EntityNotFoundException("The recipe with the id " + recipeId + " does not exist!");
        }

        Recipe recipe = optionalRecipe.get();
        NutrientsDto nutrientsDto = getTotalNutrients(recipe);

        return recipeMapper.mapModelToDetailDto(recipe, nutrientsDto);
    }

    @Override
    public void updateFavoriteStatus(Long recipeId) throws ServiceException {
        Optional<Recipe> optionalRecipe;
        String errorMessage = "The favorite status of the recipe could not be updated!";
        try {
            optionalRecipe = recipeRepository.findById(recipeId);
        } catch (Exception e) {
            throw new ServiceException(errorMessage, e);
        }
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            Boolean currentFavoriteStatus = recipe.getFavorite();
            recipe.setFavorite(!currentFavoriteStatus);
            try {
                recipeRepository.save(recipe);
            } catch (Exception e) {
                throw new ServiceException(errorMessage, e);

            }
        } else {
            throw new EntityNotFoundException("The recipe you are trying to update does not exist!");
        }


    }

    @Override
    public List<GetRecipeListItemDto> findRecipesByNutritionalValues(RecipeFilterDto dto) throws ServiceException {
        final Double EPSILON_PERCENTAGE = dto.getEpsilon() / 100.0;

        List<Recipe> allRecipes;
        try {
            allRecipes = recipeRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Recipes could not be filtered!");
        }

        Long targetProteins = dto.getProteins();
        Long targetFats = dto.getFats();
        Long targetCarbohydrates = dto.getCarbohydrates();
        Long targetCalories = dto.getCalories();

        // Berechnen der Abweichung pro Filter
        double epsilonProteins = targetProteins != null ? targetProteins * EPSILON_PERCENTAGE : 0.0;
        double epsilonFats = targetFats != null ? targetFats * EPSILON_PERCENTAGE : 0.0;
        double epsilonCarbohydrates = targetCarbohydrates != null ? targetCarbohydrates * EPSILON_PERCENTAGE : 0.0;
        double epsilonCalories = targetCalories != null ? targetCalories * EPSILON_PERCENTAGE : 0.0;

        List<GetRecipeListItemDto> getRecipeListItemDtoList = new ArrayList<>();

        for (Recipe recipe : allRecipes) {
            NutrientsDto nutrientsDto = getTotalNutrients(recipe);

            // Vergleichen der berechneten Nährwerte mit den Benutzereingaben
            if ((targetCalories == null || Math.abs(nutrientsDto.getCalories() - targetCalories) <= epsilonCalories) &&
                    (targetProteins == null || Math.abs(nutrientsDto.getProteins() - targetProteins) <= epsilonProteins) &&
                    (targetCarbohydrates == null || Math.abs(nutrientsDto.getCarbohydrates() - targetCarbohydrates) <= epsilonCarbohydrates) &&
                    (targetFats == null || Math.abs(nutrientsDto.getFats() - targetFats) <= epsilonFats)) {
                getRecipeListItemDtoList.add(recipeMapper.mapModelToListItemDto(recipe, nutrientsDto));
            }
        }

        // Sortiert die Treffer nach Genauigkeit
        Collections.sort(getRecipeListItemDtoList, Comparator.comparingDouble(recipe -> calculateEuclideanDistance(recipe, dto)));

        return getRecipeListItemDtoList;
    }



    @Override
    public void createRecipe(CreateRecipeDto dto) throws ServiceException {
        Recipe recipe = recipeMapper.mapCreateDtoToEntity(dto);
        saveRecipe(recipe);
        List<RecipeIngredient> recipeIngredientList = new ArrayList<>();
        for (RecipeIngredientDto ingredientDto : dto.getIngredients()) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setRecipe(recipe);
            Ingredient ingredient = getIngredient(ingredientDto.getFoodId());
            recipeIngredient.setIngredient(ingredient);
            // Finden der Einheit und seinen Gramm wert: also wie viel gramm bspw. 1 cup sind, das wird mit der quantity verrechnet
            Double quantityInOriginalMeasure = ingredientDto.getQuantity();
            String unit = ingredientDto.getUnit();
            IngredientUnit ingredientUnit = validateUnit(ingredient.getIngredientUnits(), unit);
            Double quantityInGrams = getQuantityConvertedToGram(ingredientUnit.getValue(), quantityInOriginalMeasure);
            recipeIngredient.setQuantity(quantityInGrams);


            String quantityString;
            if (quantityInOriginalMeasure == Math.floor(quantityInOriginalMeasure)) {
                int quantityInt = quantityInOriginalMeasure.intValue(); // Werte wie 123,0 werden zu 123g
                quantityString = String.valueOf(quantityInt);
            } else {
                quantityString = String.valueOf(quantityInOriginalMeasure);
            }
            String title = quantityString + " " + unit + " " + ingredientDto.getName();
            recipeIngredient.setTitle(title);
            recipeIngredientList.add(recipeIngredient);


        }
        saveRecipeIngredients(recipeIngredientList);

    }


    // === UTIL===

    /**
     * Berechnet die gerundeten Gesamtnaehrwerte fuer ein Rezept.
     *
     * @param recipe Das Rezept
     * @return Ein {@link NutrientsDto}-Objekt mit den Gesamtnaehrwerten
     */
    protected NutrientsDto getTotalNutrients(Recipe recipe) {
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbohydrates = 0.0;
        double totalCalories = 0.0;

        for (RecipeIngredient ingredient : recipe.getRecipeIngredients()) {
            double quantity = ingredient.getQuantity();
            Ingredient ingredientData = ingredient.getIngredient();

            totalCalories += ((ingredientData.getCalories() * quantity) / 100);
            totalProteins += ((ingredientData.getProteins() * quantity) / 100);
            totalCarbohydrates += ((ingredientData.getCarbohydrates() * quantity) / 100);
            totalFats += ((ingredientData.getFats() * quantity) / 100);


        }

        // Runden auf ganze Zahlen
        long roundedCalories = Math.round(totalCalories);
        long roundedProteins = Math.round(totalProteins);
        long roundedCarbohydrates = Math.round(totalCarbohydrates);
        long roundedFats = Math.round(totalFats);
        return NutrientsDto.builder().calories(roundedCalories).proteins(roundedProteins).carbohydrates(roundedCarbohydrates).fats(roundedFats).build();
    }


    /**
     * Berechnet den euklidischen Abstand zwischen den Nährwerten eines Rezepts und den gewünschten Nährwerten.
     *
     * @param recipe          Das Rezept, für das der Abstand berechnet werden soll.
     * @param targetNutrients Das Objekt mit den gewünschten Nährwerten.
     * @return Der euklidische Abstand zwischen den Nährwerten des Rezepts und den gewünschten Nährwerten.
     */
    protected double calculateEuclideanDistance(GetRecipeListItemDto recipe, RecipeFilterDto targetNutrients) {
        NutrientsDto recipeNutrients = recipe.getNutrients();

        Long recipeProteins = recipeNutrients.getProteins();
        Long recipeFats = recipeNutrients.getFats();
        Long recipeCarbohydrates = recipeNutrients.getCarbohydrates();
        Long recipeCalories = recipeNutrients.getCalories();


        Long targetProteins = targetNutrients.getProteins();
        Long targetFats = targetNutrients.getFats();
        Long targetCarbohydrates = targetNutrients.getCarbohydrates();
        Long targetCalories = targetNutrients.getCalories();


        double squaredDistance = 0.0;

        //Berechnung des euklidischen Abstands: nur für die Werte, die vom Benutzer angegeben wurden
        if (targetProteins != null) {
            squaredDistance += Math.pow(targetProteins - recipeProteins, 2);
        }
        if (targetFats != null) {
            squaredDistance += Math.pow(targetFats - recipeFats, 2);
        }
        if (targetCarbohydrates != null) {
            squaredDistance += Math.pow(targetCarbohydrates - recipeCarbohydrates, 2);
        }
        if (targetCalories != null) {
            squaredDistance += Math.pow(targetCalories - recipeCalories, 2);
        }


        return Math.sqrt(squaredDistance);
    }


    /**
     * Speichert ein Rezept in der Datenbank.
     *
     * @param recipe Das zu speichernde Rezept
     * @throws ServiceException Wenn ein Fehler beim Speichern auftritt
     */
    protected void saveRecipe(Recipe recipe) throws ServiceException {
        try {
            recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new ServiceException("The recipe with the title " + recipe.getTitle() + " could not be created!", e);
        }
    }

    /**
     * Speichert eine Liste von Rezept-Zutaten-Beziehungen in der Datenbank.
     *
     * @param recipeIngredients Die zu speichernde Liste von Rezept-Zutaten-Beziehungen
     * @throws ServiceException Wenn ein Fehler beim Speichern auftritt
     */
    protected void saveRecipeIngredients(List<RecipeIngredient> recipeIngredients) throws ServiceException {
        try {
            recipeIngredientRepository.saveAll(recipeIngredients);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateIngredientException("Each ingredient can only occur once per recipe!", e);
        } catch (Exception e) {
            throw new ServiceException("The recipe with the title " + recipeIngredients.get(0).getRecipe().getTitle() + " could not be created!", e);
        }
    }


    /**
     * Gibt die Zutat mit der angegebenen ID zurück.
     *
     * @param foodId Die ID der Zutat
     * @return Das {@link Ingredient}-Objekt
     * @throws ServiceException Wenn ein Fehler beim Suchen auftritt oder die Zutat nicht existiert
     */
    protected Ingredient getIngredient(String foodId) throws ServiceException {
        Optional<Ingredient> ingredient;
        try {
            ingredient = ingredientRepository.findById(foodId);
        } catch (Exception e) {
            throw new ServiceException("The recipe could not be created", e);

        }
        if (ingredient.isEmpty()) {
            throw new EntityNotFoundException("The ingredient with the id " + foodId + " does not exist!");
        }


        return ingredient.get();
    }


    /**
     * Ueberprueft, ob die angegebene Einheit in den zulaessigen Einheiten der Zutat enthalten ist.
     *
     * @param ingredientUnits Die zulaessigen Einheiten der Zutat
     * @param unit            Die angegebene Einheit
     * @return Das {@link IngredientUnit}-Objekt für die angegebene Einheit
     * @throws EntityNotFoundException Wenn die Einheit nicht zulaessig ist
     */
    protected IngredientUnit validateUnit(List<IngredientUnit> ingredientUnits, String unit) throws EntityNotFoundException {
        String ingredientName = ingredientUnits.get(0).getIngredient().getName();
        return ingredientUnits.stream().filter(ingredientUnit -> ingredientUnit.getLabel().equals(unit)).findFirst().orElseThrow(() -> new EntityNotFoundException("The unit " + unit + " does not exist for the ingredient " + ingredientName + "!"));
    }

    /**
     * Konvertiert die angegebene Menge in der angegebenen Einheit in Gramm.
     *
     * @param unitValue Die im Rezept verwendete Einheit fuer diese Zutat
     * @param quantity  Die Menge der Zutat, in der Einheit, die auch im Rezept angegeben ist
     * @return Die Menge in Gramm
     */
    protected Double getQuantityConvertedToGram(Double unitValue, Double quantity) {
        return unitValue * quantity;
    }


}