package de.burak_dogan.bachelorarbeitbackend.recipes.controller;

import de.burak_dogan.bachelorarbeitbackend.recipes.controller.dto.GetIngredientDto;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.IIngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Der IngredientController behandelt Anfragen im Zusammenhang mit Zutaten.
 * Er bietet einen Endpunkt zum Abrufen aller Zutaten aus der Rezeptdatenbank.
 */
@Slf4j
@RestController
@RequestMapping(value = IngredientController.BASE_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class IngredientController {
    /**
     * Die Basis-URL des IngredientControllers.
     */
    protected final static String BASE_URL = "/api/v1/ingredients";

    /**
     * Der Service, der die Geschaeftslogik fuer die Verarbeitung der Anfragen bereitstellt.
     */
    private final IIngredientService service;

    /**
     * Erzeugt einen neuen IngredientController mit dem angegebenen IIngredientService.
     *
     * @param service der zu verwendende IIngredientService
     */
    @Autowired
    public IngredientController(IIngredientService service) {
        this.service = service;
    }

    /**
     * Ruft alle Zutaten aus der Rezeptdatenbank ab.
     *
     * @return ResponseEntity mit einer Liste von GetIngredientDto, den Zutaten
     * @throws ServiceException bei einem Fehler in der Service-Schicht
     */
    @GetMapping
    public ResponseEntity<List<GetIngredientDto>> getIngredients() throws ServiceException {
        List<GetIngredientDto> ingredients = service.getIngredients();
        return ResponseEntity.ok(ingredients);
    }
}
