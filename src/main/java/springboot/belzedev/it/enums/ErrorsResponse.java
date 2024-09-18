package springboot.belzedev.it.enums;

/**
 * Enumerazione per i codici di errore utilizzati nelle risposte delle API.
 * Fornisce un elenco standard di errori che possono essere restituiti dalle API
 * per rappresentare diversi tipi di errori di validazione e problemi del server.
 */
public enum ErrorsResponse {

    INVALID_EMAIL,
    EXISTS_EMAIL,
    INVALID_USERNAME,
    EXISTS_USERNAME,
    SHORT_PSW,
    INVALID_PSW,
    SERVER_ERROR
}
