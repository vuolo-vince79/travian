package springboot.belzedev.it.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.belzedev.it.models.User;

import java.util.Optional;

/**
 * Questa interfaccia estende {@link JpaRepository} per gestire le operazioni CRUD e query personalizzate
 * per l'entità {@link User}. Il framework Spring Data JPA fornisce l'implementazione automatica delle
 * operazioni di base sui dati.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Verifica se esiste un utente con l'email specificata.
     *
     * @param email L'email dell'utente da verificare
     * @return {@code true} se esiste un utente con l'email specificata, {@code false} altrimenti
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se esiste un utente con lo username specificato.
     *
     * @param username Lo username dell'utente da verificare
     * @return {@code true} se esiste un utente con lo username specificato, {@code false} altrimenti
     */
    boolean existsByUsername(String username);

    /**
     * Trova un utente in base allo username.
     *
     * @param username Lo username dell'utente da trovare
     * @return Un {@link Optional} contenente l'utente, se trovato, o vuoto se l'utente non esiste
     */
    Optional<User> findByUsername(String username);

    /**
     * Esegue una query SQL nativa per registrare un nuovo utente inserendo email, username e password nella tabella.
     * La query è annotata con {@link Modifying} per indicare che si tratta di una modifica ai dati.
     * La transazione è gestita con {@link Transactional} per garantire l'integrità dei dati.
     *
     * @param email L'email del nuovo utente
     * @param username Lo username del nuovo utente
     * @param psw La password del nuovo utente
     */
    @Modifying
    @Transactional
    @Query(value = "insert into users(email, username, psw)values(:email, :username, :psw)", nativeQuery = true)
    void register(@Param("email") String email, @Param("username") String username, @Param("psw") String psw);
}
