package springboot.belzedev.it.exceptions;

/**
 * Eccezione personalizzata che viene lanciata quando si cerca di ricercare
 * uno username non presente nel sistema
 * Estende {@link RuntimeException} per consentire la gestione di errori in fase di esecuzione.
 */
public class InvalidUsernameException extends RuntimeException{

    /**
     * Costruttore per creare un'istanza di {@code InvalidUsernameException} con un messaggio di errore specifico.
     *
     * @param message Il messaggio di errore che descrive il motivo per cui è stata lanciata l'eccezione.
     */
    public InvalidUsernameException(String message){
        super(message);
    }
}
