package de.burak_dogan.bachelorarbeitbackend.recipes.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Eine benutzerdefinierte Ausnahme, die ausgeloest wird, wenn ein Fehler bei der JSON-Verarbeitung auftritt.
 * Diese Ausnahme erweitert die Klasse JsonProcessingException aus der Jackson-Bibliothek.
 */
public class JsonParseException extends JsonProcessingException {

    /**
     * Konstruktor fuer die JsonParseException.
     *
     * @param msg Die Fehlermeldung, die den Grund fuer die Ausnahme beschreibt.
     */
    public JsonParseException(String msg) {
        super(msg);
    }
}