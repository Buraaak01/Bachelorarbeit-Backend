package de.burak_dogan.bachelorarbeitbackend.recipes.exception;


/**
 * Eine benutzerdefinierte Exception, die ausgeloest wird, wenn eine ungueltige Masseinheit beim Mappen der importierten Rezepte erkannt wird.
 * Die invaliden Masseinheiten sind in der InvalidMeasure.java Datei vorzufinden.
 */
public class InvalidMeasureException extends JsonParseException {

    /**
     * Konstruktor fuer die InvalidMeasureException.
     *
     * @param msg die Fehlermeldung, die die ungueltige Masseinheit beschreibt
     */
    public InvalidMeasureException(String msg) {
        super(msg);
    }
}
