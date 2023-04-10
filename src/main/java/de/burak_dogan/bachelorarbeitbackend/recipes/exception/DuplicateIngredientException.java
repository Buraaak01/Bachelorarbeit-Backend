package de.burak_dogan.bachelorarbeitbackend.recipes.exception;

/**
 * Eine benutzerdefinierte Ausnahme, die ausgeloest wird, wenn ein Duplikat eines Ingredients innerhalb desselben Rezepts festgestellt wird.
 */
public class DuplicateIngredientException extends ServiceException {

    /**
     * Konstruktor fuer die DuplicateIngredientException.
     *
     * @param message die Fehlermeldung, die die Ursache der Ausnahme beschreibt.
     * @param cause   die Ausnahme, die diese Ausnahme ausgeloest hat.
     */
    public DuplicateIngredientException(String message, Throwable cause) {
        super(message, cause);
    }
}
