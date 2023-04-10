package de.burak_dogan.bachelorarbeitbackend.recipes.exception;


/**
 * Eine benutzerdefinierte Ausnahme, die in der Service-Schicht geworfen wird, um allgemeine Service-Fehler zu repraesentieren.
 */
public class ServiceException extends Exception {

    /**
     * Konstruktor fuer die ServiceException mit einer Fehlermeldung und einem Ausloesegrund.
     *
     * @param message Die Fehlermeldung, die den Grund fuer die Ausnahme beschreibt.
     * @param cause   Der Ausloesegrund, der diese Ausnahme verursacht hat.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Konstruktor fuer die ServiceException mit einer Fehlermeldung.
     *
     * @param message Die Fehlermeldung, die den Grund fuer die Ausnahme beschreibt.
     */
    public ServiceException(String message) {
        super(message);
    }
}
