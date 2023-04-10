package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper;

import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Ingredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.IngredientUnit;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Recipe;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.RecipeIngredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model.IngredientMappingDTO;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model.IngredientUnitMappingDTO;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model.RecipeIngredientMappingDTO;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model.RecipeMappingDTO;
import org.springframework.stereotype.Component;

/**
 * Diese Klasse dient dazu, die MappingDTOs aus dem DataImporterService in Entities umzuwandeln, die im jeweiligen Repository gespeichert werden koennen.
 */
@Component
public class InitialDataMapper {

    /**
     * Wandelt ein RecipeMappingDTO-Objekt und ein Bild (als Byte-Array) in eine Recipe-Entity um.
     *
     * @param recipeMappingDTO Das RecipeMappingDTO-Objekt, das in eine Recipe-Entity umgewandelt werden soll.
     * @param imageAsByte   Das Bild als Byte-Array, das der Recipe-Entity zugeordnet werden soll.
     * @return Die erstellte Recipe-Entity.
     */
    public Recipe mapToRecipe(RecipeMappingDTO recipeMappingDTO, byte[] imageAsByte){
        return Recipe.builder().title(recipeMappingDTO.getLabel()).preparation(recipeMappingDTO.getUrl()).image(imageAsByte).portions(recipeMappingDTO.getYield()).favorite(false).build();
    }


    /**
     * Wandelt ein IngredientMappingDTO-Objekt in eine Ingredient-Entity um.
     *
     * @param ingredientMappingDTO Das IngredientMappingDTO-Objekt, das in eine Ingredient-Entity umgewandelt werden soll.
     * @return Die erstellte Ingredient-Entity.
     */
    public Ingredient mapToIngredient(IngredientMappingDTO ingredientMappingDTO) {
        return Ingredient.builder().id(ingredientMappingDTO.getFoodId()).name(ingredientMappingDTO.getLabel()).calories(ingredientMappingDTO.getCalories()).proteins(ingredientMappingDTO.getProteins()).fats(ingredientMappingDTO.getFats()).carbohydrates(ingredientMappingDTO.getCarbohydrates()).build();
    }

    /**
     * Wandelt ein RecipeIngredientMappingDTO-Objekt mit dem dazugehoerigen Rezept und der Zutat in eine RecipeIngredient-Entity um.
     *
     * @param recipe                Das Recipe-Objekt, zu dem das RecipeIngredient gehoert.
     * @param ingredient            Das Ingredient-Objekt, das im RecipeIngredient enthalten ist.
     * @param recipeIngredientMappingDTO Das RecipeIngredientMappingDTO-Objekt, das in eine RecipeIngredient-Entity umgewandelt werden soll.
     * @return Die erstellte RecipeIngredient-Entity.
     */
    public RecipeIngredient mapToRecipeIngredients(Recipe recipe, Ingredient ingredient, RecipeIngredientMappingDTO recipeIngredientMappingDTO) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setTitle(recipeIngredientMappingDTO.getText());
        recipeIngredient.setQuantity(recipeIngredientMappingDTO.getQuantity());
        return recipeIngredient;
    }


    /**
     * Wandelt ein IngredientUnitMappingDTO-Objekt mit der dazugehoerigen Zutat in eine IngredientUnit-Entity um.
     *
     * @param ingredientUnitMappingDTO Das IngredientUnitMapping-Objekt, das in eine IngredientUnit-Entity umgewandelt werden soll.
     * @param ingredient            Das Ingredient-Objekt, zu dem die IngredientUnit gehoert.
     * @return Die erstellte IngredientUnit-Entity.
     */
    public IngredientUnit mapToIngredientUnit(IngredientUnitMappingDTO ingredientUnitMappingDTO, Ingredient ingredient) {
        return IngredientUnit.builder().ingredient(ingredient).label(ingredientUnitMappingDTO.getLabel()).value(ingredientUnitMappingDTO.getWeight()).build();
    }

}
