package springboot.belzedev.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.belzedev.it.dto.ApiResponse;
import springboot.belzedev.it.exceptions.EmailAlreadyExistsException;
import springboot.belzedev.it.exceptions.InvalidEmailException;
import springboot.belzedev.it.exceptions.PasswordTooShortException;
import springboot.belzedev.it.exceptions.UsernameAlreadyExistsException;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.services.UserService;

import java.util.List;

/**
 * Controller per la gestione delle operazioni sugli utenti.
 * Fornisce endpoint per ottenere, aggiungere, aggiornare e eliminare utenti.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")
public class UserController {

    // Inietta il servizio per la gestione degli utenti
    @Autowired
    private UserService userService;

    /**
     * Recupera tutti gli utenti.
     *
     * @return una lista di utenti
     */
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    /**
     * Recupera un utente specifico per ID.
     *
     * @param id l'ID dell'utente da recuperare
     * @return una risposta HTTP con lo stato e i dettagli dell'utente se trovato, altrimenti una risposta HTTP 404 (NOT FOUND)
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        User user = userService.getUser(id);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Aggiunge un nuovo utente.
     *
     * @param user l'oggetto User contenente i dettagli del nuovo utente
     * @return una risposta HTTP con lo stato dell'operazione di aggiunta
     */
    @PostMapping
    public ResponseEntity<ApiResponse> addUser(@RequestBody User user){
        try{
            // Tenta di aggiungere il nuovo utente
            userService.addUser(user);
            // Restituisce una risposta con stato 201 (CREATED) se l'utente è stato aggiunto con successo
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("User aggiunto con successo", true));
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
                    .body(new ApiResponse("Errore interno del server", false));
        }
    }

    /**
     * Aggiorna i dettagli di un utente esistente.
     *
     * @param user l'oggetto User contenente i nuovi dettagli dell'utente
     * @param id l'ID dell'utente da aggiornare
     * @return una risposta HTTP con stato 204 (NO CONTENT) se l'aggiornamento ha successo
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody User user, @PathVariable Long id){
        userService.updateUser(user, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Elimina un utente specifico per ID.
     *
     * @param id l'ID dell'utente da eliminare
     * @return una risposta HTTP con stato 204 (NO CONTENT) se l'eliminazione ha successo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
