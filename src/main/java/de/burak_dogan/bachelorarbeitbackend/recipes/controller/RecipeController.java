package de.burak_dogan.bachelorarbeitbackend.recipes.controller;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.CreateRecipeDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeDetailDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetRecipeListItemDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.RecipeFilterDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.IRecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Der RecipeController behandelt Anfragen im Zusammenhang mit Rezepten.
 * Er bietet Endpunkte zum Abrufen, Erstellen, Aktualisieren und Filtern von Rezepten.
 */
@Slf4j
@RestController
@RequestMapping(value = RecipeController.BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {

    /**
     * Der Basis-URL fuer die Rezept-Ressource.
     */
    protected final static String BASE_URL = "/api/v1/recipes";

    /**
     * Der Pfad fuer die Detailansicht eines Rezepts mit Platzhalter fuer die Rezept-ID.
     */
    protected final static String DETAIL = "/{recipeId}";

    /**
     * Der Pfad für das Aktualisieren des Favoritenstatus eines Rezepts mit Platzhalter für die Rezept-ID.
     */
    private final static String FAVORITE = "/favorites/{recipeId}";

    /**
     * Der Pfad für das Filtern von Rezepten.
     */
    private final static String FILTER = "/filter";

    /**
     * Der Service, der die Geschaeftslogik fuer die Verarbeitung der Anfragen bereitstellt.
     */
    private final IRecipeService service;


    /**
     * Erzeugt einen neuen RecipeController mit dem angegebenen IRecipeService.
     *
     * @param service der zu verwendende IRecipeService
     */
    @Autowired
    public RecipeController(IRecipeService service) {
        this.service = service;
    }

    /**
     * Ruft alle Rezepte fuer die Listenansicht ab.
     *
     * @return ResponseEntity mit einer Liste von GetRecipeListItemDto
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @GetMapping
    public ResponseEntity<List<GetRecipeListItemDto>> getRecipes() throws ServiceException {
        List<GetRecipeListItemDto> recipes = service.getRecipes();
        return ResponseEntity.ok(recipes);
    }

    /**
     * Ruft ein einzelnes Rezept fuer die Detailansicht anhand der angegebenen Rezept-ID ab.
     *
     * @param recipeId die ID des Rezepts
     * @return ResponseEntity mit GetRecipeDetailDto des abgerufenen Rezepts
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @GetMapping(value = DETAIL)
    public ResponseEntity<GetRecipeDetailDto> getSingleRecipe(@PathVariable Long recipeId) throws ServiceException {
        GetRecipeDetailDto recipe = service.getSingleRecipe(recipeId);
        return ResponseEntity.ok(recipe);
    }

    /**
     * Aktualisiert den Favoritenstatus eines Rezepts anhand der angegebenen Rezept-ID.
     *
     * @param recipeId die ID des Rezepts
     * @return ResponseEntity ohne Inhalt
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @PutMapping(value = FAVORITE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFavoriteStatus(@PathVariable Long recipeId) throws ServiceException {
        service.updateFavoriteStatus(recipeId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Ruft gefilterte Rezepte anhand der angegebenen Filterkriterien ab.
     *
     * @param recipeFilterDto das RecipeFilterDto mit den Filterkriterien
     * @return ResponseEntity mit einer Liste von GetRecipeListItemDto der abgerufenen Rezepte
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @GetMapping(value = FILTER)
    public ResponseEntity<List<GetRecipeListItemDto>> getFilteredRecipes(@Valid @ModelAttribute RecipeFilterDto recipeFilterDto) throws ServiceException {
        List<GetRecipeListItemDto> recipes = service.findRecipesByNutritionalValues(recipeFilterDto);
        return ResponseEntity.ok(recipes);
    }


    /**
     * Erstellt ein neues Rezept.
     *
     * @param dto das CreateRecipeDto mit den Informationen fuer das neue Rezept
     * @return ResponseEntity ohne Inhalt
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @PostMapping
    public ResponseEntity<Void> createRecipe(@RequestBody @Valid CreateRecipeDto dto) throws ServiceException {
        service.createRecipe(dto);
        return ResponseEntity.noContent().build();
    }


}
