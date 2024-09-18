package springboot.belzedev.it.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.repository.UserRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Inietta il repository che si occupa di accedere ai dati degli utenti
    @Autowired
    private UserRepository userRepository;

    /**
     * Metodo sovrascritto di `UserDetailsService` per caricare i dettagli di un utente dato lo username.
     *
     * @param username Lo username dell'utente da cercare.
     * @return Un oggetto `UserDetails` contenente le informazioni dell'utente.
     * @throws UsernameNotFoundException Se l'utente con lo username specificato non viene trovato.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca l'utente nel database tramite il repository usando lo username
        User user = userRepository.findByUsername(username)
                // Se l'utente non viene trovato, lancia un'eccezione UsernameNotFoundException
                .orElseThrow(() -> new UsernameNotFoundException("User non trovato con username: " + username));
        // Restituisce un oggetto UserDetails per l'utente trovato
        // Spring Security richiede un oggetto User che contenga username, password e autorizzazioni
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPsw(), new ArrayList<>());
        // Nota: Attualmente viene restituita una lista vuota di permessi (authorities), ma questo può essere esteso per includere i ruoli dell'utente
    }
}
