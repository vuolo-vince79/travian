package springboot.belzedev.it.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Component
public class JwtUtil {

    // Chiave segreta generata dinamicamente per firmare i token JWT utilizzando l'algoritmo HS256
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Metodo per generare un token JWT.
     *
     * @param username Il nome utente dell'utente per cui viene generato il token.
     * @param idUser L'ID dell'utente, aggiunto come "claim" nel token.
     * @return Una stringa contenente il token JWT generato.
     */
    public String generateToken(String username, Long idUser){
        // Creazione del token JWT con:
        // - subject (nome utente)
        // - claim personalizzato (ID utente)
        // - data di emissione
        // - data di scadenza (10 ore dall'ora corrente)
        // - firma con la chiave segreta
        return Jwts.builder()
                .setSubject(username)  // Imposta il subject come nome utente
                .claim("idUser", idUser)  // Aggiunge il claim "idUser" con l'ID dell'utente
                .setIssuedAt(new Date())  // Imposta la data di emissione
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 3600 * 1000))  // Imposta la scadenza a 10 ore
                .signWith(SECRET_KEY)  // Firma il token con la chiave segreta
                .compact();  // Compattta tutto in una stringa JWT
    }

    /**
     * Metodo privato per estrarre tutte le informazioni (claims) dal token JWT.
     *
     * @param token Il token da cui estrarre i claims.
     * @return Un oggetto Claims che contiene tutte le informazioni del token.
     */
    private Claims extractAllClaims(String token){
        // Estrae i claims (dati) dal token JWT usando la chiave segreta per decodificarlo
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)  // Imposta la chiave segreta per la verifica del token
                .build()
                .parseClaimsJws(token)  // Decodifica il token
                .getBody();  // Ottiene il corpo (claims) del token
    }

    /**
     * Metodo per estrarre il nome utente (subject) dal token JWT.
     *
     * @param token Il token da cui estrarre il nome utente.
     * @return Il nome utente contenuto nel token.
     */
    public String extractUsername(String token){
        // Estrae il subject, che rappresenta il nome utente
        return extractAllClaims(token).getSubject();
    }

    /**
     * Metodo per estrarre l'ID utente dal token JWT.
     *
     * @param token Il token da cui estrarre l'ID utente.
     * @return L'ID utente contenuto nel claim "idUser" del token.
     */
    public Long extractIdUser(String token){
        Claims claims = extractAllClaims(token);
        // Estrae il claim "idUser" come Long
        return claims.get("idUser", Long.class);
    }

    /**
     * Metodo per verificare se il token JWT è scaduto.
     *
     * @param token Il token da controllare.
     * @return true se il token è scaduto, false altrimenti.
     */
    public boolean isTokenExpired(String token){
        Claims claims = extractAllClaims(token);
        // Verifica se la data di scadenza è prima della data corrente
        return claims.getExpiration().before(new Date());
    }

    /**
     * Metodo per convalidare un token JWT.
     *
     * @param token Il token da convalidare.
     * @param username Il nome utente da confrontare con quello contenuto nel token.
     * @return true se il token è valido (nome utente corretto e non scaduto), false altrimenti.
     */
    public boolean validateToken(String token, String username){
        // Controlla se il nome utente nel token corrisponde a quello fornito e se il token non è scaduto
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    /**
     * Metodo privato per ottenere la chiave di firma.
     * Potrebbe essere utile se in futuro si vuole esporre la chiave in qualche contesto.
     *
     * @return La chiave segreta utilizzata per firmare i token JWT.
     */
    private SecretKey getSigningKey() {
        return SECRET_KEY;
    }

}
