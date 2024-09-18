package springboot.belzedev.it.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springboot.belzedev.it.security.JwtRequestFilter;
import springboot.belzedev.it.services.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    // Inietta il servizio customizzato per la gestione degli UserDetails
    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Inietta il filtro JWT che intercetta le richieste per verificare i token
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configura la catena di filtri di sicurezza per l'applicazione.
     * Disabilita il CSRF, consente tutte le richieste HTTP e aggiunge il filtro JWT prima di quello di autenticazione.
     *
     * @param http l'oggetto HttpSecurity per configurare le autorizzazioni e i filtri di sicurezza
     * @return il SecurityFilterChain configurato
     * @throws Exception se si verifica un errore durante la configurazione
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // Disabilita il CSRF e consente tutte le richieste HTTP
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Aggiunge il filtro JWT prima del filtro standard di autenticazione
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Definisce il bean BCryptPasswordEncoder per codificare le password degli utenti.
     * Questo encoder viene utilizzato per criptare le password e confrontarle durante l'autenticazione.
     *
     * @return un'istanza di BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        // Restituisce un'istanza di BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    /**
     * Crea e restituisce un AuthenticationManager, necessario per gestire il processo di autenticazione.
     * L'AuthenticationManager è responsabile della validazione delle credenziali fornite dagli utenti.
     *
     * @param httpSecurity l'oggetto HttpSecurity usato per configurare la sicurezza dell'applicazione
     * @return l'AuthenticationManager configurato
     * @throws Exception se si verifica un errore durante la configurazione
     */
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{
        // Recupera l'AuthenticationConfiguration dall'oggetto HttpSecurity
        AuthenticationConfiguration authenticationConfiguration = httpSecurity.getSharedObject(AuthenticationConfiguration.class);
        // Restituisce l'AuthenticationManager associato alla configurazione
        return authenticationConfiguration.getAuthenticationManager();
    }

}
