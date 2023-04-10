package de.burak_dogan.bachelorarbeitbackend.recipes.exception;


/**
 * Eine benutzerdefinierte Ausnahme, die ausgeloest wird, wenn ein JSON-Schluessel nicht gefunden wird.
 */
public class JsonKeyNotFoundException extends JsonParseException {

    /**
     * Konstruktor fuer die JsonKeyNotFoundException.
     *
     * @param msg Die Fehlermeldung, die die fehlende JSON-Schluesselangabe beschreibt.
     */
    public JsonKeyNotFoundException(String msg) {
        super(msg);
    }
}
