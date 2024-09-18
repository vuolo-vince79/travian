package springboot.belzedev.it.exceptions;

/**
 * Eccezione personalizzata che viene lanciata quando si cerca di registrare un'email
 * che già esiste nel sistema.
 * Estende {@link RuntimeException} per consentire la gestione di errori in fase di esecuzione.
 */
public class EmailAlreadyExistsException extends RuntimeException{

    /**
     * Costruttore per creare un'istanza di {@code EmailAlreadyExistsException} con un messaggio di errore specifico.
     *
     * @param message Il messaggio di errore che descrive il motivo per cui è stata lanciata l'eccezione.
     */
    public EmailAlreadyExistsException(String message){
        super(message);
    }
}
