package springboot.belzedev.it.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.belzedev.it.enums.ErrorsResponse;
import springboot.belzedev.it.exceptions.EmailAlreadyExistsException;
import springboot.belzedev.it.exceptions.InvalidEmailException;
import springboot.belzedev.it.exceptions.PasswordTooShortException;
import springboot.belzedev.it.exceptions.UsernameAlreadyExistsException;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service per gestire la logica di business dell'entità {@link User}.
 * Contiene operazioni CRUD e metodi di validazione per l'aggiunta e la gestione degli utenti.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Restituisce la lista di tutti gli utenti presenti nel database.
     *
     * @return Lista di utenti
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Restituisce un utente in base all'ID. Se l'utente non viene trovato, restituisce {@code null}.
     *
     * @param id ID dell'utente da cercare
     * @return {@link User} se trovato, altrimenti {@code null}
     */
    public User getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    /**
     * Aggiunge un nuovo utente con validazioni.
     * Esegue controlli sull'email, username e password, e se validi, cripta la password e salva l'utente nel database.
     *
     * @param user Utente da aggiungere
     * @throws Exception Se si verificano errori durante la validazione
     */
    public void addUser(@Valid User user) throws Exception {
        if(!isValidEmail(user.getEmail())){
            throw new InvalidEmailException(ErrorsResponse.INVALID_EMAIL.name());
        }
        else if(userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException(ErrorsResponse.EXISTS_EMAIL.name());
        }
        else if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistsException(ErrorsResponse.EXISTS_USERNAME.name());
        }
        else if(user.getPsw().length() < 8){
            throw new PasswordTooShortException(ErrorsResponse.SHORT_PSW.name());
        }
        // Cripta la password dell'utente
        user.setPsw(bCryptPasswordEncoder.encode(user.getPsw()));
        // Imposta la lingua e il tema di default
        user.setLang("en");
        user.setTheme(false);
        // Salva l'utente nel database
        userRepository.save(user);
    }

    /**
     * Aggiorna un utente esistente con un nuovo ID.
     *
     * @param user Utente da aggiornare
     * @param id ID dell'utente da aggiornare
     */
    public void updateUser(User user, Long id){
        user.setId_user(id);
        userRepository.save(user);
    }

    /**
     * Cancella un utente in base all'ID, se esiste.
     *
     * @param id ID dell'utente da cancellare
     */
    public void deleteUser(Long id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(value -> userRepository.delete(value));
    }

    /**
     * Valida un'email utilizzando una regex.
     *
     * @param email L'email da validare
     * @return {@code true} se l'email è valida, {@code false} altrimenti
     */
    private boolean isValidEmail(String email) {
        // Regex per una validazione base dell'email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}
