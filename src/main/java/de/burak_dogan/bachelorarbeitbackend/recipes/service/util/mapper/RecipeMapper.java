package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.CreateRecipeDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeDetailDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.NutrientsDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeListItemDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Recipe;
import org.springframework.stereotype.Component;

/**
 * Diese Klasse dient dazu, eine Recipe-Entity in ein DTO umzuwandeln.
 */
@Component
public class RecipeMapper {

    /**
     * Wandelt ein Recipe-Objekt in ein GetRecipeListItemDto um, welches fuer die Listenansicht verwendet wird.
     *
     * @param recipe       Das Recipe-Objekt, das in ein DTO umgewandelt werden soll.
     * @param nutrientsDto Das NutrientsDto-Objekt, das die Naehrwerte fuer das Rezept enthaelt.
     * @return Das erstellte GetRecipeListItemDTO.
     */
    public GetRecipeListItemDto mapModelToListItemDto(Recipe recipe, NutrientsDto nutrientsDto) {
        return GetRecipeListItemDto.builder().id(recipe.getId()).title(recipe.getTitle()).image(recipe.getImage()).portions(recipe.getPortions()).favorite(recipe.getFavorite()).nutrients(nutrientsDto).build();
    }

    /**
     * Wandelt ein Recipe-Objekt in ein GetRecipeDetailDto um, welches fuer die Detailansicht verwendet wird.
     *
     * @param recipe       Das Recipe-Objekt, das in ein DTO umgewandelt werden soll.
     * @param nutrientsDto Das NutrientsDto-Objekt, das die Naehrwerte fuer das Rezept enthaelt.
     * @return Das erstellte GetRecipeDetailDTO.
     */
    public GetRecipeDetailDto mapModelToDetailDto(Recipe recipe, NutrientsDto nutrientsDto) {
        return GetRecipeDetailDto.builder().id(recipe.getId()).title(recipe.getTitle()).image(recipe.getImage()).portions(recipe.getPortions()).favorite(recipe.getFavorite()).nutrients(nutrientsDto).preparation(recipe.getPreparation()).ingredients(recipe.getRecipeIngredients().stream().map(ingredient -> ingredient.getTitle()).toList()).build();
    }

    /**
     * Wandelt ein CreateRecipeDto in eine Recipe-Entity um, damit diese für Repository-Aufrufe genutzt werden kann.
     *
     * @param dto       Das CreateRecipeDto-Objekt, das in eine Entity umgewandelt werden soll.
     * @return Die erstellte Recipe-Entity.
     */
    public Recipe mapCreateDtoToEntity(CreateRecipeDto dto) {
        return Recipe.builder().title(dto.getTitle()).image(dto.getImage()).portions(dto.getPortions()).preparation(dto.getPreparation()).favorite(false).build();
    }

}
