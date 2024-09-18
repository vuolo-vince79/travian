package springboot.belzedev.it.exceptions;

/**
 * Eccezione personalizzata che viene lanciata quando si cerca di registrare una password
 * che non ha il numero minimo di caratteri richiesti.
 * Estende {@link RuntimeException} per consentire la gestione di errori in fase di esecuzione.
 */
public class PasswordTooShortException extends RuntimeException{

    /**
     * Costruttore per creare un'istanza di {@code PasswordTooShortException} con un messaggio di errore specifico.
     *
     * @param message Il messaggio di errore che descrive il motivo per cui è stata lanciata l'eccezione.
     */
    public PasswordTooShortException(String message){
        super(message);
    }
}
