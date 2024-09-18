package springboot.belzedev.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springboot.belzedev.it.dto.ApiResponse;
import springboot.belzedev.it.enums.ErrorsResponse;
import springboot.belzedev.it.exceptions.InvalidPasswordException;
import springboot.belzedev.it.exceptions.InvalidUsernameException;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.repository.UserRepository;
import springboot.belzedev.it.services.CustomUserDetailsService;
import springboot.belzedev.it.services.LoginRegisterService;
import springboot.belzedev.it.utils.JwtUtil;

import java.util.Map;

/**
 * Controller per la gestione delle richieste di autenticazione (login).
 * Fornisce un endpoint per la creazione di un token di autenticazione JWT.
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/login")
public class LoginController {

    // Inietta il servizio personalizzato per caricare i dettagli dell'utente
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Inietta il componente JwtUtil per la generazione dei token JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Inietta il componente BCryptPasswordEncoder per la gestione delle password
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Inietta il repository per l'accesso ai dati degli utenti
    @Autowired
    private UserRepository userRepository;

    @Autowired
    LoginRegisterService loginRegisterService;

    /**
     * Autentica l'utente e genera un token JWT.
     *
     * @Param credential Un oggetto Map che contiene le credenziali di accesso dell'utente (username e password).
     * @Return Un ResponseEntity che contiene il token JWT se l'autenticazione ha successo,
     *         o un messaggio di errore in caso di fallimento.
     * @Throws InvalidUsernameException Se l'username non esiste nel database.
     * @Throws InvalidPasswordException Se la password non corrisponde.
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createAuthenticationToken(@RequestBody Map<String, String> credential) throws Exception{
        try{
            // Estrae username e password dalle credenziali e chiama il service per autenticare l'utente
            User user = loginRegisterService.login(credential.get("username"), credential.get("psw"));
            // Carica i dettagli dell'utente, necessari per il token JWT
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            // Genera il token JWT per l'utente autenticato
            final String token = jwtUtil.generateToken(userDetails.getUsername(), user.getId_user());
            // Restituisce una risposta HTTP 201 Created con il token generato in caso di successo
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(token, true));
        }
        catch (InvalidUsernameException | InvalidPasswordException e){
            // Se c'è un'eccezione (username o password errati), restituisce una risposta HTTP 400 Bad Request
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
