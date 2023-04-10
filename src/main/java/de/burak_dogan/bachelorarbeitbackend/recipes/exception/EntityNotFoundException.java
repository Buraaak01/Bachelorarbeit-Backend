package de.burak_dogan.bachelorarbeitbackend.recipes.exception;


/**
 * Eine benutzerdefinierte Ausnahme, die ausgeloest wird, wenn eine Entitaet nicht in der Datenbank gefunden werden kann.
 */
public class EntityNotFoundException extends ServiceException {

    /**
     * Konstruktor fuer die EntityNotFoundException.
     *
     * @param message die Fehlermeldung, die die Ursache der Ausnahme beschreibt.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }



}