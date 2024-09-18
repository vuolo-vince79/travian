package springboot.belzedev.it.exceptions;

/**
 * Eccezione personalizzata che viene lanciata quando si cerca di registrare uno username
 * che è già presente nel sistema.
 * Estende {@link RuntimeException} per consentire la gestione di errori in fase di esecuzione.
 */
public class UsernameAlreadyExistsException extends RuntimeException{

    /**
     * Costruttore per creare un'istanza di {@code UsernameAlreadyExistsException} con un messaggio di errore specifico.
     *
     * @param message Il messaggio di errore che descrive il motivo per cui è stata lanciata l'eccezione.
     */
    public UsernameAlreadyExistsException(String message){
        super(message);
    }
}
