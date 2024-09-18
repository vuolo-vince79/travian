package springboot.belzedev.it.dto;

/**
 * Classe di trasferimento dati (DTO) per la risposta API.
 * Viene utilizzata per restituire un messaggio e uno stato di successo o errore come risposta alle richieste API.
 */
public class ApiResponse {

    // Messaggio che fornisce dettagli sul risultato dell'operazione (es. "Utente aggiunto con successo", "Errore interno del server")
    private String message;

    // Stato di successo dell'operazione: true se l'operazione è riuscita, false altrimenti
    private boolean success;

    /**
     * Costruttore della classe ApiResponse.
     *
     * @param message Il messaggio da includere nella risposta
     * @param success Il flag di successo che indica se l'operazione è riuscita o meno
     */
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
