package springboot.belzedev.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.belzedev.it.dto.ApiResponse;
import springboot.belzedev.it.enums.ErrorsResponse;
import springboot.belzedev.it.exceptions.EmailAlreadyExistsException;
import springboot.belzedev.it.exceptions.InvalidEmailException;
import springboot.belzedev.it.exceptions.PasswordTooShortException;
import springboot.belzedev.it.exceptions.UsernameAlreadyExistsException;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.services.LoginRegisterService;

/**
 * Controller per la gestione delle richieste di registrazione degli utenti.
 * Fornisce un endpoint per registrare nuovi utenti.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/register")
public class RegisterController {

    // Inietta il servizio per la registrazione e gestione degli utenti
    @Autowired
    private LoginRegisterService loginRegisterService;

    /**
     * Gestisce la richiesta di registrazione di un nuovo utente.
     * Registra l'utente e restituisce una risposta HTTP con lo stato dell'operazione.
     *
     * @param user l'oggetto User contenente le informazioni dell'utente da registrare
     * @return una risposta HTTP con uno stato e un messaggio che indica il risultato dell'operazione
     */
    @PostMapping
    public ResponseEntity<ApiResponse> register(@RequestBody User user){
        try{
            // Tenta di registrare l'utente utilizzando il servizio di registrazione
            loginRegisterService.register(user);
            // Restituisce una risposta con stato 201 (CREATED) se la registrazione ha successo
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Utente aggiunto con successo", true));
        }
        catch (EmailAlreadyExistsException | PasswordTooShortException | InvalidEmailException
               | UsernameAlreadyExistsException e){
            // Gestisce le eccezioni specifiche e restituisce una risposta con stato 400 (BAD REQUEST) con il messaggio dell'eccezione
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
        catch (Exception e){
            // Gestisce eventuali altre eccezioni e restituisce una risposta con stato 400 (BAD REQUEST) e un messaggio di errore generico
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(ErrorsResponse.SERVER_ERROR.name(), false));
        }
    }
}
