package de.burak_dogan.bachelorarbeitbackend.recipes.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.naming.ServiceUnavailableException;

/**
 * Eine zentrale Klasse zum Abfangen von Exceptions und Zurueckgeben einer entsprechenden Fehlermeldung als ResponseEntity.
 * Diese Klasse ist mit der Annotation {@link ControllerAdvice} gekennzeichnet, um global auf Ausnahmen zu reagieren.
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {


    /**
     * Behandelt eine ServiceException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurueck.
     *
     * @param e die aufgetretene ServiceException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorObject> handleException(ServiceException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorObject errorObject = new ErrorObject(internalServerError, e.getMessage());

        return new ResponseEntity<>(errorObject, internalServerError);
    }

    /**
     * Behandelt eine EntityNotFoundException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurueck.
     *
     * @param e die aufgetretene EntityNotFoundException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleException(EntityNotFoundException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorObject errorObject = new ErrorObject(notFound, e.getMessage());

        return new ResponseEntity<>(errorObject, notFound);
    }

    /**
     * Behandelt eine ServiceUnavailableException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurueck.
     * Wird geworfen, wenn versucht wird auf eine Servicemethode zuzugreifen, waehrend die Rezepte noch importiert werden.
     *
     * @param e die aufgetretene ServiceUnavailableException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorObject> handleException(ServiceUnavailableException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus serviceUnavailable = HttpStatus.SERVICE_UNAVAILABLE;
        ErrorObject errorObject = new ErrorObject(serviceUnavailable, e.getMessage());

        return new ResponseEntity<>(errorObject, serviceUnavailable);
    }

    /**
     * Behandelt eine DuplicateIngredientException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurueck.
     *
     * @param e die aufgetretene DuplicateIngredientException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(DuplicateIngredientException.class)
    public ResponseEntity<ErrorObject> handleException(DuplicateIngredientException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus conflict = HttpStatus.CONFLICT;
        ErrorObject errorObject = new ErrorObject(conflict, e.getMessage());

        return new ResponseEntity<>(errorObject, conflict);
    }




    /**
     * Behandelt eine MissingServletRequestParameterException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurueck.
     * Die MissingServletRequestParameterException wird dann geworfen, wenn ein Request-Parameter fehlt.
     *
     * @param e die aufgetretene MissingServletRequestParameterException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorObject> handleException(MissingServletRequestParameterException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        String errorMessage = "Parameter "+ e.getParameterName() + " is missing";
        ErrorObject errorObject = new ErrorObject(badRequest, errorMessage);

        return new ResponseEntity<>(errorObject, badRequest);
    }

    /**
     * Behandelt eine MethodArgumentTypeMismatchException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurück.
     * Die MethodArgumentTypeMismatchException wird dann geworfen, wenn eine Pfadvariable oder ein Request-Parameter einen falschen Typen hat.
     *
     * @param e die aufgetretene MethodArgumentTypeMismatchException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorObject> handleException(MethodArgumentTypeMismatchException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        String errorMessage = "Parameter "+ e.getPropertyName() + " has a wrong type";
        ErrorObject errorObject = new ErrorObject(badRequest, errorMessage);

        return new ResponseEntity<>(errorObject, badRequest);
    }

    /**
     * Behandelt eine MethodArgumentNotValidException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurück.
     * Die MethodArgumentNotValidException wird dann geworfen, wenn die Validierung eines Methodenarguments fehlschlaegt.
     *
     * @param e die aufgetretene MethodArgumentNotValidException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage="";

        if(e.getGlobalError()!=null){
            errorMessage = e.getGlobalError().getDefaultMessage();
        } else{
            if(e.getFieldError()!=null){
            errorMessage = e.getFieldError().getDefaultMessage();
            }
        }
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());

        ErrorObject errorObject = new ErrorObject(badRequest, errorMessage);
        return new ResponseEntity<>(errorObject, badRequest);


    }

    /**
     * Behandelt eine HttpMessageNotReadableException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurück.
     * Die HttpMessageNotReadableException wird dann geworfen, wenn beim Lesen einer HTTP-Nachricht ein Fehler auftritt.
     * Beispielsweise, wenn beim Erstellen eines Rezepts ein ungueltiger ResponseBody gesendet wird.
     *
     * @param e die aufgetretene HttpMessageNotReadableException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorObject> handleException(HttpMessageNotReadableException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        String errorMessage = "Request value contains invalid format";
        ErrorObject errorObject = new ErrorObject(badRequest, errorMessage);

        return new ResponseEntity<>(errorObject, badRequest);
    }

    /**
     * Behandelt eine HttpMediaTypeNotSupportedException und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurück.
     * Die HttpMediaTypeNotSupportedException wird dann geworfen, wenn beim Erstellen der Request ein falscher Content-Type angegeben wird.
     *
     * @param e die aufgetretene HttpMediaTypeNotSupportedException
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorObject> handleException(HttpMediaTypeNotSupportedException e) {
        log.debug(String.valueOf(e.getCause()));
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        log.error(e.getMessage());
        String errorMessage = e.getMessage()+"! Please provide the content-type application/json";
        ErrorObject errorObject = new ErrorObject(badRequest, errorMessage);

        return new ResponseEntity<>(errorObject, badRequest);
    }


    /**
     * Behandelt eine allgemeine Exception und gibt eine ResponseEntity mit einem entsprechenden ErrorObject zurück.
     *
     * @param e die aufgetretene Exception
     * @return die ResponseEntity mit dem {@link ErrorObject}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleException(Exception e) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage());
        ErrorObject errorObject = new ErrorObject(internalServerError, "Unknown error");

        return new ResponseEntity<>(errorObject, internalServerError);
    }



}
