package springboot.belzedev.it.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

/**
 * Questa classe rappresenta l'entità {@code User} che viene mappata alla tabella {@code users} nel database.
 * La classe contiene i campi che rappresentano le informazioni di un utente, come l'email, username, password, tema e lingua.
 * I campi sono associati alla tabella tramite le annotazioni JPA.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificativo univoco dell'utente, mappato alla colonna {@code id_user} nella tabella {@code users}.
     * Viene generato automaticamente con la strategia {@code GenerationType.IDENTITY}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    /**
     * L'email dell'utente, mappata alla colonna {@code email}.
     * Potrebbe includere una validazione tramite l'annotazione {@link Email}.
     */
    private String email;

    /**
     * Username dell'utente, mappato alla colonna {@code username}.
     */
    private  String username;

    /**
     * Password dell'utente, mappata alla colonna {@code psw}.
     */
    private String psw;

    /**
     * Verifica dell'attivazione account dall'email dell'utente
     */
    private boolean verified;

    /**
     * Preferenza di tema dell'utente, mappata alla colonna {@code theme}.
     * Rappresenta un valore booleano, dove {@code true} potrebbe indicare un tema scuro e {@code false} un tema chiaro.
     */
    private boolean theme;

    /**
     * Impostazione della lingua dell'utente, mappata alla colonna {@code lang}.
     */
    private String lang;

    /**
     * Costruttore di default senza parametri.
     * Necessario per il corretto funzionamento di JPA.
     */
    public User(){}

    /**
     * Costruttore con tutti i parametri tranne l'ID, utilizzato per creare un nuovo utente.
     *
     * @param email Email dell'utente
     * @param username Username dell'utente
     * @param psw Password dell'utente
     * @param theme Preferenza del tema dell'utente
     * @param lang Impostazione della lingua dell'utente
     */
    public User(String email, String username, String psw, boolean verified, boolean theme, String lang) {
        this.email = email;
        this.username = username;
        this.psw = psw;
        this.verified = verified;
        this.theme = theme;
        this.lang = lang;
    }

    /**
     * Costruttore con tutti i parametri, incluso l'ID, utilizzato per aggiornare un utente esistente.
     *
     * @param id_user ID univoco dell'utente
     * @param email Email dell'utente
     * @param username Username dell'utente
     * @param psw Password dell'utente
     * @param theme Preferenza del tema dell'utente
     * @param lang Impostazione della lingua dell'utente
     */
    public User(Long id_user, String email, String username, String psw, boolean verified, boolean theme, String lang) {
        this.id_user = id_user;
        this.email = email;
        this.username = username;
        this.psw = psw;
        this.verified = verified;
        this.theme = theme;
        this.lang = lang;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw.trim();
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean getTheme() {
        return theme;
    }

    public void setTheme(boolean theme) {
        this.theme = theme;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
