package de.burak_dogan.bachelorarbeitbackend.recipes.config;

import de.burak_dogan.bachelorarbeitbackend.recipes.exception.ServiceException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspekt zur UEberpruefung der Initialisierung des Systems, dem Import von Rezepten.
 */
@Aspect
@Component
public class InitializationAspect {

    /**
     * Dieser Wert gibt an, ob die Initialisierung im Gange ist oder nicht.
     * Der Standard Wert ist 'true', da bei Anwendungsstart die initialen Daten ueberprueft und wenn noetig gespeichert werden muessen.
     * Ist dieser Prozess fertig, wird der Wert ueber den Setter auf 'false' gesetzt, sodass Anfragen nun empfangen werden koennen.
     *
     */
    private boolean initializationInProgress = true;

    /**
     * Vorberatungsmethode, die vor jedem Aufruf einer Service-Methode aufgerufen wird,
     * um die Initialisierung der Anwendung zu ueberpruefen.
     *
     * @throws ServiceException Wenn die Initialisierung noch im Gange ist.
     */
    @Before("execution(* de.burak_dogan.bachelorarbeitbackend.recipes.service.*.*(..)) && !within(de.burak_dogan.bachelorarbeitbackend.recipes.service.DataImporterService)")
    public void checkInitialization() throws ServiceException {
        if (initializationInProgress) {
            throw new ServiceException("The recipes and ingredients are still loading, please try again later!");
        }
    }

    /**
     * Setter-Methode fuer die Eigenschaft 'initializationInProgress'.
     *
     * @param initializationInProgress Der Wert, der angibt, ob die Initialisierung im Gange ist oder nicht.
     */
    public void setInitializationInProgress(boolean initializationInProgress) {
        this.initializationInProgress = initializationInProgress;
    }
}
