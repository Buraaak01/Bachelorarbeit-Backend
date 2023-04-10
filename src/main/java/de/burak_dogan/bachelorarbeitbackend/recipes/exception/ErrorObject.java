package de.burak_dogan.bachelorarbeitbackend.recipes.exception;

import org.springframework.http.HttpStatus;

/**
 * Ein Record, der Informationen ueber einen Fehler enthaelt, der als Response in einer ResponseEntity zurueckgegeben wird.
 */
public record ErrorObject(HttpStatus httpStatus, String errorMsg) {

}
