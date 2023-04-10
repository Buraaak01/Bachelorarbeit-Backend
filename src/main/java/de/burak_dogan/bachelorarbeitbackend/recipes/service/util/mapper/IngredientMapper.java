package de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetIngredientDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Ingredient;
import org.springframework.stereotype.Component;

/**
 * Diese Klasse dient dazu, eine Ingredient-Entity in ein DTO umzuwandeln.
 */
@Component
public class IngredientMapper {


    /**
     * Wandelt eine Ingredient-Entity in ein GetIngredientDto um.
     *
     * @param ingredient Das Ingredient-Objekt, das in ein GetIngredientDto umgewandelt werden soll.
     * @return Das erstellte GetIngredientDTO.
     */
    public GetIngredientDto mapModelToDto(Ingredient ingredient) {
        return GetIngredientDto.builder().foodId(ingredient.getId()).name(ingredient.getName()).units(ingredient.getIngredientUnits().stream().map(ingredientUnit -> ingredientUnit.getLabel()).toList()).build();
    }
}
