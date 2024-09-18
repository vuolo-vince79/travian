package springboot.belzedev.it.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.belzedev.it.enums.ErrorsResponse;
import springboot.belzedev.it.exceptions.*;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.repository.UserRepository;

import java.util.Optional;

@Service
public class LoginRegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService tokenService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Registra un nuovo utente nel sistema dopo aver eseguito diverse validazioni.
     *
     * @Param user L'oggetto User contenente i dettagli dell'utente (email, username, password) da registrare.
     * @Throws InvalidEmailException Se l'email fornita non è valida.
     * @Throws EmailAlreadyExistsException Se l'email è già presente nel database.
     * @Throws UsernameAlreadyExistsException Se lo username è già presente nel database.
     * @Throws PasswordTooShortException Se la password è più corta di 8 caratteri.
     */
    public void register(@Valid User user) throws Exception {
        // Verifica se l'email fornita è valida
        if(!isValidEmail(user.getEmail())){
            throw new InvalidEmailException(ErrorsResponse.INVALID_EMAIL.name());
        }
        // Verifica se l'email esiste già nel database
        else if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException(ErrorsResponse.EXISTS_EMAIL.name());
        }
        // Verifica se lo username esiste già nel database
        else if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistsException(ErrorsResponse.EXISTS_USERNAME.name());
        }
        // Verifica se la password è abbastanza lunga (minimo 8 caratteri)
        else if(user.getPsw().length() < 8){
            throw new PasswordTooShortException(ErrorsResponse.SHORT_PSW.name());
        }
        // Se tutte le validazioni sono passate, cripta la password
        user.setPsw(bCryptPasswordEncoder.encode(user.getPsw()));
        // Registra l'utente nel database con email, username e password criptata
        userRepository.register(user.getEmail(), user.getUsername(), user.getPsw());

        User addedUser = userRepository.findByUsername(user.getUsername()).get();
        tokenService.createVerificationToken(addedUser);

        String token = tokenService.getTokenForUser(addedUser);
        emailService.sendVerificationEmail(addedUser.getEmail(), token);

    }

    /**
     * Autentica l'utente in base al nome utente e alla password forniti.
     *
     * @Param username Il nome utente fornito dall'utente per l'autenticazione.
     * @Param password La password fornita dall'utente per l'autenticazione.
     * @Return L'oggetto User autenticato, se username e password sono validi.
     * @Throws InvalidUsernameException Se l'username non esiste nel database.
     * @Throws InvalidPasswordException Se la password non corrisponde.
     */
    public User login(String username, String password){
        // Cerca l'utente nel database in base al nome utente
        Optional<User> optionalUser = userRepository.findByUsername(username);
        // Verifica se l'utente esiste nel database
        if(optionalUser.isPresent()){
            // Recupera l'utente trovato
            User user = optionalUser.get();
            // Confronta la password fornita con quella memorizzata nel database utilizzando BCrypt
            if(bCryptPasswordEncoder.matches(password, user.getPsw())){
                // Se la password è corretta, restituisce l'utente
                return  user;
            }
            // Se la password è errata, solleva un'eccezione personalizzata per password non valida
            else throw new InvalidPasswordException(ErrorsResponse.INVALID_PSW.name());
        }
        // Se l'utente non esiste, solleva un'eccezione personalizzata per username non valido
        else throw new InvalidUsernameException(ErrorsResponse.INVALID_USERNAME.name());
    }

    private boolean isValidEmail(String email) {
        // Regex per una validazione base dell'email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

}
