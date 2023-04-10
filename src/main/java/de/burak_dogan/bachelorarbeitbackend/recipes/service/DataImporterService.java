package de.burak_dogan.bachelorarbeitbackend.recipes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.burak_dogan.bachelorarbeitbackend.recipes.config.InitializationAspect;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.JsonKeyNotFoundException;
import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.IngredientUnitRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.IngredientRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.RecipeIngredientRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.RecipeRepository;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Ingredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.IngredientUnit;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.Recipe;
import de.burak_dogan.bachelorarbeitbackend.recipes.repository.entity.RecipeIngredient;
import de.burak_dogan.bachelorarbeitbackend.recipes.config.properties.RequestProperties;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.InitialDataMapper;
import de.burak_dogan.bachelorarbeitbackend.recipes.service.util.mapper.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Diese Klasse ist verantwortlich fuer den Import von den Rezepten. Dafuer werden Requests an die Edamam-API gesendet, welche nach dem Mappen in der Datenbank gespeichert werden.
 *
 * @see <a href="https://developer.edamam.com/edamam-docs-recipe-api#/">Recipes API</a> und <a href="https://developer.edamam.com/food-database-api-docs#/">Food API</a>
 */
@Slf4j
@Service
public class DataImporterService {
    /**
     * Das Repository fuer Rezepte
     */
    private final RecipeRepository recipeRepository;

    /**
     * Das Repository fuer Zutaten
     */
    private final IngredientRepository ingredientRepository;

    /**
     * Das Repository fuer Rezept-Zutaten
     */
    private final RecipeIngredientRepository recipeIngredientRepository;

    /**
     * Das Repository fuer Zutateneinheiten
     */
    private final IngredientUnitRepository ingredientUnitRepository;

    /**
     * Der RestTemplate fuer die HTTP-Anfragen
     */
    private final RestTemplate restTemplate;

    /**
     * Die RequestProperties fuer die Anfragen
     */
    private final RequestProperties requestProperties;

    /**
     * Der {@link ObjectMapper} fuer die JSON-Verarbeitung
     */
    private final ObjectMapper objectMapper;

    /**
     * Der InitialDataMapper fuer das Mappen der DTOs zu Entities
     */
    private final InitialDataMapper initialDataMapper;

    /**
     * Der InitializationAspect, der auf true gesetzt wird, wenn die Initialisierung bzw. der Import der Daten fertig ist
     */
    private final InitializationAspect initializationAspect;

    /**
     * Der PlatformTransactionManager, der die Transaktionen verwaltet
     */
    private final PlatformTransactionManager transactionManager;


    /**
     * Konstruktor fuer den DataImporterService.
     *
     * @param recipeRepository           Das Repository fuer Rezepte
     * @param ingredientRepository       Das Repository fuer Zutaten
     * @param recipeIngredientRepository Das Repository fuer Rezept-Zutaten
     * @param ingredientUnitRepository   Das Repository fuer Zutateneinheiten
     * @param restTemplate               Der RestTemplate fuer die HTTP-Anfragen
     * @param requestProperties          Die RequestProperties fuer die Anfragen
     * @param objectMapper               Der ObjectMapper fuer die JSON-Verarbeitung
     * @param initialDataMapper          Der InitialDataMapper fuer das Mappen der DTOs zu Entities
     * @param initializationAspect       Der InitializationAspect, der auf true gesetzt wird, wenn die Initialisierung bzw. der Import der Daten fertig ist
     * @param transactionManager         Der PlatformTransactionManager, der die Transaktionen verwaltet
     */
    @Autowired
    public DataImporterService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeIngredientRepository recipeIngredientRepository, IngredientUnitRepository ingredientUnitRepository, RequestProperties requestProperties, RestTemplate restTemplate, ObjectMapper objectMapper, InitialDataMapper initialDataMapper, InitializationAspect initializationAspect, PlatformTransactionManager transactionManager) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientUnitRepository = ingredientUnitRepository;
        this.restTemplate = restTemplate;
        this.requestProperties = requestProperties;
        this.objectMapper = objectMapper;
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true); // Objectmapper so konfiguriert, dass Eintraege die null als wert haben, nicht gemappt werden, und eine Exception werfen.
        this.initialDataMapper = initialDataMapper;
        this.initializationAspect = initializationAspect;
        this.transactionManager = transactionManager;
    }


    /**
     * Diese Methode initialisiert die Daten, indem sie Rezepte von einer externen API abruft und in der Datenbank speichert.
     * Die Methode wird als Event-Listener fuer das ApplicationReadyEvent registriert und wird automatisch ausgefuehrt,
     * wenn die Anwendung vollstaendig gestartet und bereit ist. Es werden keine Exceptions geworfen, da das Programm beim Auftreten eines Fehlers weiterlaufen soll.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        int iterationCount = 0;
        int maxIterations = 12;
        List<Recipe> savedRecipes = new ArrayList<>();

        int minEntries = 40;   //mindestens 40 Rezept-Einträge müssen in der DB gespeichert sein
        URI recipeUrl = URI.create("https://api.edamam.com/api/recipes/v2?type=public&q=&field=label&field=image&field=url&field=yield&field=ingredientLines&field=ingredients&app_id=" + requestProperties.getRecipeAppId() + "&app_key=" + requestProperties.getRecipeAppKey() + "&diet=high-protein");

        while (savedRecipes.size() <= minEntries) { //So lange iterieren, bis mindestens 40 Einträge gespeichert wurden
            boolean noLink = false;
            iterationCount++;
            Long currentEntries = 0L;
            try {
                currentEntries = getRecipeCount();
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }
            if (currentEntries >= minEntries) {
                // Im Prinzip, selbe Abfrage wie bei der while-Bedingung. Nötig, da ,,savedRecipes" nur
                // die Rezepte enthält, die in dieser Session gepeichert wurden, nicht alle Rezepte aus der DB
                // Falls also aus einer früheren Abfrage Mal bspw. 20 Daten in der DB verblieben sind, und in der Session 20 weitere gespeichert worden sind,
                // dann würde das Abbruchkriterium nicht erfüllt sein, obwohl der Mindestwert an rezepte in der datenbank erreicht wurde

                log.info(currentEntries + " recipes haven been saved in total");
                break;
            } else if (iterationCount > maxIterations) { //Umgehen einer Unendlichkeitsschleife; insgesamt 12 versuche die Mindestanazhl an einträgen zu speichern
                log.error("Exceeded maximum iterations. " + currentEntries + " recipes haven been saved in total");
                break;
            }
            log.info("Current iteration: " + iterationCount);

            RecipeContainerDTO recipeContainerDTO = getResponse(recipeUrl);
            log.info(String.valueOf(recipeContainerDTO));
            if (recipeContainerDTO == null) {
                //Get Request fehlgeschlagen
                break;
            }
            List<RecipeMappingDTO> recipeMappingDTOList = recipeContainerDTO.getRecipeMappingDTOList();
            if (StringUtils.isEmpty(recipeContainerDTO.getLink())) {
                noLink = true; //gehe nach Verarbeiten der Daten raus aus der Schleife, da es keine weiteren Einträge mehr gibt
            } else {
                recipeUrl = URI.create(recipeContainerDTO.getLink());
            }
            if (recipeMappingDTOList == null || recipeMappingDTOList.isEmpty()) {
                continue; //wenn keine hits, mach mit der nächsten seite weiter
            }


            for (RecipeMappingDTO recipeMappingDTO : recipeMappingDTOList) {
                Recipe savedRecipe = save(recipeMappingDTO);
                if (savedRecipe != null) {
                    savedRecipes.add(savedRecipe);

                }
            }
            if (noLink) {
                log.info("No more response data. " + savedRecipes.size() + " entities have been saved");
                break;
            }

        }
        initializationAspect.setInitializationInProgress(false);
        if (savedRecipes.size() >= minEntries) {
            log.info(savedRecipes.size() + " recipes have been saved");
        }


    }

    /**
     * Diese Methode ruft eine API-Response von der angegebenen URL ab.
     *
     * @param recipeUrl URL der Rezept-API-Anfrage
     * @return API-Response als RecipeContainerDTO-Objekt
     */
    protected RecipeContainerDTO getResponse(URI recipeUrl) {
        ResponseEntity<JsonNode> response;
        log.info("URL: " + recipeUrl);
        try {
            response = restTemplate.getForEntity(recipeUrl, JsonNode.class);
            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Error during Get Request!");
                return null;
            }
        } catch (Exception e) {
            log.info("Error during Get Request! " + e.getMessage());
            return null;
        }
        RecipeContainerDTO recipeContainerDTO = new RecipeContainerDTO();

        hasNextPage(response, recipeContainerDTO);

        if (!response.getBody().has("hits")) {
            log.error("Response in: " + recipeUrl + " does not contain any valid recipes.");
            return recipeContainerDTO.with(Collections.emptyList());
        }

        JsonNode hits = response.getBody().get("hits");
        List<RecipeMappingDTO> recipeMappingDTOList = new ArrayList<>();
        recipeLoop:
        for (JsonNode recipeHit : hits) {
            RecipeMappingDTO recipeMappingDTO = mapValidRecipeEntries(recipeHit);

            if (recipeMappingDTO == null) {
                continue; //mache nur mit den rezepten weiter, die valide sind, daher continue
            }
            for (RecipeIngredientMappingDTO recipeIngredientMappingDTO : recipeMappingDTO.getRecipeIngredientMappingDTOList()) {
                IngredientMappingDTO ingredientMappingDTO;
                String foodId = recipeIngredientMappingDTO.getFoodId();
                ingredientMappingDTO = getIngredient(foodId, recipeIngredientMappingDTO.getMeasure());

                if (ingredientMappingDTO == null) {
                    //Nicht alle Zutaten konnten gemappt werden
                    continue recipeLoop; //mache nur mit den rezepten weiter, die valide sind, sprich alle Zutaten enthalten, daher continue
                }
                recipeIngredientMappingDTO.setQuantity(recipeIngredientMappingDTO.getQuantity() * ingredientMappingDTO.getUnitInGrams());
                recipeIngredientMappingDTO.setIngredientMappingDTO(ingredientMappingDTO);
            }
            recipeMappingDTOList.add(recipeMappingDTO);

        }
        return recipeContainerDTO.with(recipeMappingDTOList);
    }


    /**
     * Überprüft, ob die Antwort einen naechsten Seitenlink enthaelt, und setzt ihn in das RecipeContainerDTO-Objekt.
     *
     * @param response           Die ResponseEntity mit der JSON-Antwort der Request
     * @param recipeContainerDTO Das RecipeContainerDTO-Objekt, in das der naechste Seitenlink gesetzt wird
     */
    protected static void hasNextPage(ResponseEntity<JsonNode> response, RecipeContainerDTO recipeContainerDTO) {
        if (response.getBody().has("_links")) {
            JsonNode links = response.getBody().get("_links");
            if (links.has("next")) {
                JsonNode next = links.get("next");
                if (next.has("href")) {
                    String nextPageUrl = next.get("href").asText();
                    recipeContainerDTO.with(nextPageUrl);
                }
            }
        }
    }


    /**
     * Mapt die gueltigen Rezepteintraege aus der JSON-Antwort in ein RecipeMappingDTO-Objekt.
     *
     * @param response Die JSON-Antwort
     * @return Das gemappte RecipeMappingDTO-Objekt oder null, wenn Rezept nicht gueltig ist und somit nicht gemappt werden kann
     */
    protected RecipeMappingDTO mapValidRecipeEntries(JsonNode response) {
        if (!response.has("recipe")) {
            return null;
        }
        try {
            return objectMapper.readValue(response.get("recipe").toString(), RecipeMappingDTO.class);
        } catch (JsonProcessingException e) {
            log.warn(e.getOriginalMessage());

            return null; //Es wird keine Exception geworfen, damit die Abfrage weiterläuft
        }


    }

    /**
     * Ruft die Informationen für eine Zutat anhand ihrer Food-ID ab.
     *
     * @param foodId Die Food-ID der Zutat
     * @param unit   Die Einheit der Zutat aus dem Rezept
     * @return Das gemappte IngredientMappingDTO-Objekt oder null, wenn Zutat nicht gueltig ist und somit nicht gemappt werden kann
     */
    protected IngredientMappingDTO getIngredient(String foodId, String unit) {
        //Pro Zutat Pro Rezept
        String baseURL = "https://api.edamam.com/api/food-database/v2/parser";
        String ingredientUrl = baseURL + "?app_id=" + requestProperties.getIngredientAppId() + "&app_key=" + requestProperties.getIngredientAppKey() + "&ingr=" + foodId + "&nutrition-type=cooking";
        ResponseEntity<JsonNode> response;
        try {
            response = restTemplate.getForEntity(ingredientUrl, JsonNode.class);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                log.info("Error during Get Request! ");
                return null;
            }
        } catch (Exception e) {
            log.info("Error during Get Request! " + e.getMessage());
            return null;
        }

        if (!response.getBody().has("hints")) {
            log.error("Response does not contain any valid ingredients.");
            return null;
        }
        JsonNode ingredient = response.getBody().get("hints").get(0);
        return mapValidIngredientEntries(ingredient, foodId, unit);


    }

    /**
     * Mapt die gültigen Zutateneintraege aus der JSON-Antwort in ein IngredientMappingDTO-Objekt.
     *
     * @param response Die JSON-Antwort der Request
     * @param foodId   Die Food-ID der Zutat
     * @param unit     Die Einheit der Zutat aus dem Rezept
     * @return Das gemappte IngredientMappingDTO-Objekt oder null, wenn Zutat nicht gueltig ist und somit nicht gemappt werden kann
     */
    protected IngredientMappingDTO mapValidIngredientEntries(JsonNode response, String foodId, String unit) {
        try {
            if (!response.has("food")) {
                throw new JsonKeyNotFoundException("Key 'food' does not exist");
            }
            IngredientMappingDTO ingredientMappingDTO = objectMapper.readValue(response.get("food").toString(), IngredientMappingDTO.class);
            List<IngredientUnitMappingDTO> units = findEveryUnit(response);
            ingredientMappingDTO.setIngredientUnitMappingDTOS(units);
            if (unit.equals("<unit>")) {
                unit = "Whole";
            }
            String finalUnit = unit;
            IngredientUnitMappingDTO unitInGrams = units.stream().filter(unitMapping -> unitMapping.getLabel().equalsIgnoreCase(finalUnit)).findFirst().orElseThrow(() -> new JsonKeyNotFoundException("Key 'units' is invalid! " + finalUnit + " does not exist!"));
            ingredientMappingDTO.setUnitInGrams(unitInGrams.getWeight());
            return ingredientMappingDTO;
        } catch (JsonProcessingException e) {
            log.warn("Ingredient with foodId: " + foodId + " could not be mapped! " + e.getMessage());
            return null; //Es wird keine Exception geworfen, damit die Abfrage weiterläuft
        }


    }

    /**
     * Sucht alle Einheiten einer Zutat in der JSON-Antwort der Request und mappt sie in eine Liste von IngredientUnitMappingDTO-Objekten.
     *
     * @param response Die JSON-Antwort
     * @return Die Liste der gemappten IngredientUnitMappingDTO-Objekte
     * @throws JsonKeyNotFoundException Wenn ein erforderlicher Schlüssel in der JSON-Antwort nicht vorhanden ist
     */
    protected List<IngredientUnitMappingDTO> findEveryUnit(JsonNode response) throws JsonKeyNotFoundException {
        List<IngredientUnitMappingDTO> ingredientUnitMappingDTOS = new ArrayList<>();
        if (!response.has("measures")) {
            throw new JsonKeyNotFoundException("Key 'measures' does not exist");
        }

        JsonNode units = response.get("measures");
        for (JsonNode unitNode : units) {
            if (unitNode.has("weight")) {
                if (unitNode.has("uri")) {
                    if (!unitNode.get("uri").asText().endsWith("default")) {
                        String label = unitNode.get("label").asText();
                        Double weight = unitNode.get("weight").asDouble();
                        ingredientUnitMappingDTOS.add(IngredientUnitMappingDTO.builder().label(label).weight(weight).build());
                    }
                }

            } else {
                throw new JsonKeyNotFoundException("Key 'units' is invalid!");
            }
        }
        return ingredientUnitMappingDTOS;


    }

    /**
     * Diese Methode speichert ein Rezept mit den dazugehoerigen Zutaten in der Datenbank.
     *
     * @param recipeMappingDTO Rezept-Faten als RecipeMappingDTO-Objekt
     * @return gespeichertes Rezept als Recipe-Objekt
     */
    protected Recipe save(RecipeMappingDTO recipeMappingDTO) {
        AtomicReference<Recipe> savedRecipeRef = new AtomicReference<>();
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.execute(status -> {
            try {
                byte[] imageAsByte = retrieveImage(recipeMappingDTO.getImage());
                Recipe recipe = initialDataMapper.mapToRecipe(recipeMappingDTO, imageAsByte);
                saveRecipe(recipe);
                List<RecipeIngredientMappingDTO> recipeIngredientMappingDTOList = recipeMappingDTO.getRecipeIngredientMappingDTOList();
                for (RecipeIngredientMappingDTO recipeIngredientMappingDTO : recipeIngredientMappingDTOList) {
                    IngredientMappingDTO ingredientMappingDTO = recipeIngredientMappingDTO.getIngredientMappingDTO();
                    Ingredient ingredient = initialDataMapper.mapToIngredient(ingredientMappingDTO);
                    boolean ingredientExistsAlready = saveIngredient(ingredient);
                    saveRecipeIngredient(initialDataMapper.mapToRecipeIngredients(recipe, ingredient, recipeIngredientMappingDTO));
                    if (!ingredientExistsAlready) {
                        //speichere die Einheiten nur, wenn im selben Zug die dazugehörige zutat gespeichert wurde,
                        //sowohl Zutat als auch Einheiten werden also nicht gespeichert, wenn die Zutat bereits in der DB existiert
                        //denn, wenn die Zutat existiert, existieren auch immer die dazugehörigen Einheiten

                        for (IngredientUnitMappingDTO ingredientUnitMappingDTO : ingredientMappingDTO.getIngredientUnitMappingDTOS()) {

                            saveUnit(initialDataMapper.mapToIngredientUnit(ingredientUnitMappingDTO, ingredient));

                        }
                    }
                }
                savedRecipeRef.set(recipe); // Speichere die im repository erfolgreich hinzugefügte Zutat
            } catch (ServiceException e) {
                status.setRollbackOnly();
            }
            return null;
        });

        return savedRecipeRef.get(); // Gib das gespeicherte Rezept aus
    }


    /**
     * Diese Methode speichert ein Rezept, ohne ihre Zutaten.
     *
     * @param recipe Das zu speichernde Rezept als Recipe-Objekt
     * @throws ServiceException Wenn ein Fehler beim Zugriff auf die Datenbank auftritt
     */
    protected void saveRecipe(Recipe recipe) throws ServiceException {
        try {
            Optional<Recipe> savedRecipe = recipeRepository.findByTitle(recipe.getTitle()); // alle vorhandenen Rezepte mit dem Titel abrufen
            if (savedRecipe.isPresent()) {
                //schaut, ob Rezept mit dem Titel bereits existiert, -> verhindert, dass dasselbe Rezept mehrmals vorkommt
                throw new ServiceException("Recipe already exists.");
            } else {
                recipeRepository.save(recipe);
            }
        } catch (Exception e) {
            String errorMessage = "Recipe  " + recipe.getTitle() + " could not be saved!";
            log.error(errorMessage + " " + e.getMessage());
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * Diese Methode ruft die Anzahl der gespeicherten Rezepte aus der Datenbank ab.
     *
     * @return Anzahl der gespeicherten Rezepte
     * @throws ServiceException falls ein Fehler beim Zugriff auf die Datenbank auftritt
     */
    @Transactional(readOnly = true, rollbackFor = ServiceException.class)
    protected Long getRecipeCount() throws ServiceException {
        try {
            return recipeRepository.count();
        } catch (Exception e) {
            String errorMessage = "Recipe count could not be loaded ";
            throw new ServiceException(errorMessage, e);
        }
    }


    /**
     * Speichert eine Zutat in der Datenbank.
     *
     * @param ingredient Die zu speichernde Zutat als Ingredient-Objekt
     * @return Gibt zurück, ob die Zutat bereits vorhanden war (true) oder erfolgreich gespeichert wurde (false)
     * @throws ServiceException Wenn ein Fehler beim Zugriff auf die Datenbank auftritt
     */
    protected boolean saveIngredient(Ingredient ingredient) throws ServiceException {
        try {
            List<String> existingIds = ingredientRepository.findAllIds(); // alle vorhandenen Zutaten-IDs abrufen
            if (!existingIds.contains(ingredient.getId())) {
                ingredientRepository.save(ingredient); // Zutat nur speichern, wenn ID noch nicht vorhanden ist
                return false;
            } else {
                log.info("Ingredient " + ingredient.getName() + " with the id " + ingredient.getId() + "was not saved, it already exists!");
                return true;
            }
        } catch (Exception e) {
            String errorMessage = "Ingredient with the id: " + ingredient.getId() + " could not be saved!";
            log.error(errorMessage + " " + e.getMessage());
            throw new ServiceException(errorMessage, e);
        }

    }


    /**
     * Speichert eine Rezeptzutat in der Datenbank. Also die Zuweisung einer Zutat zu einem Rezept
     *
     * @param recipeIngredient Die zu speichernde Rezeptzutat als RecipeIngredient-Objekt
     * @throws ServiceException Wenn ein Fehler beim Zugriff auf die Datenbank auftritt
     */
    protected void saveRecipeIngredient(RecipeIngredient recipeIngredient) throws ServiceException {
        try {
            recipeIngredientRepository.save(recipeIngredient);
        } catch (Exception e) {
            String errorMessage = "Recipe " + recipeIngredient.getRecipe().getTitle() + " and it's ingredient " + recipeIngredient.getIngredient().getName() + " could not be saved!";
            log.error(errorMessage + " " + e.getMessage());
            throw new ServiceException(errorMessage, e);
        }
    }

    /**
     * Speichert die Masseinheit für eine Zutat in der Datenbank.
     *
     * @param ingredientUnit Das zu speichernde IngredientUnit-Objekt
     * @throws ServiceException Wenn ein Fehler beim Speichern der Einheiten auftritt
     */
    protected void saveUnit(IngredientUnit ingredientUnit) throws ServiceException {
        String ingredientId = ingredientUnit.getIngredient().getId();
        try {
            ingredientUnitRepository.save(ingredientUnit);

        } catch (Exception e) {
            String errorMessage = "IngredientMeasures for ingredient " + ingredientId + "with the unit "
                    + ingredientUnit.getLabel() + " and it's value " + ingredientUnit.getValue() + " could not be saved!";
            log.error(errorMessage + " " + e.getMessage());
            throw new ServiceException(errorMessage, e);
        }
    }


    /**
     * Ruft ein Bild von der angegebenen URL ab und gibt es als Byte-Array zurück.
     *
     * @param imageUrl Die URL des Bildes
     * @return Das abgerufene Bild als Byte-Array
     * @throws ServiceException Wenn ein Fehler beim Abrufen des Bildes auftritt
     */
    protected byte[] retrieveImage(String imageUrl) throws ServiceException {
        try {
            // Es wurde sich an diesem Ansatz orientiert: https://stackoverflow.com/a/2295331
            URIBuilder uriBuilder = new URIBuilder(imageUrl);
            URL url = uriBuilder.build().toURL();

            InputStream inputStream = url.openStream(); //Daten werden aus der URL gelesen
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) { //
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("Error while retrieving image: " + e.getMessage());
        }


    }

}
