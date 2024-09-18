package springboot.belzedev.it.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.belzedev.it.utils.JwtUtil;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Inietta il componente JwtUtil, utilizzato per gestire i token JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Inietta il servizio UserDetailsService per caricare i dettagli dell'utente
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Filtro che intercetta ogni richiesta HTTP e verifica la presenza di un token JWT valido.
     * Se il token è valido, autentica l'utente e aggiorna il SecurityContext.
     *
     * @param request la richiesta HTTP corrente
     * @param response la risposta HTTP corrente
     * @param filterChain la catena di filtri che devono essere eseguiti successivamente
     * @throws ServletException se si verifica un errore nella gestione della richiesta
     * @throws IOException se si verifica un errore di input/output durante il filtraggio
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // Estrae l'header "Authorization" dalla richiesta
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        // Verifica se l'header contiene un token JWT e se inizia con "Bearer"
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            // Rimuove il prefisso "Bearer " per ottenere il token
            jwtToken = authorizationHeader.substring(7);
            try{
                // Estrae lo username dal token JWT
                username = jwtUtil.extractUsername(jwtToken);
            }
            catch(ExpiredJwtException e){
                // Gestisce il caso in cui il token è scaduto
                System.out.println("Token scaduto");
            }
        }
        // Se c'è un username estratto e l'utente non è già autenticato
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // Carica i dettagli dell'utente dal servizio UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // Valida il token JWT confrontando lo username e verificando se è scaduto
            if(jwtUtil.validateToken(jwtToken, username)){
                // Crea un'istanza di UsernamePasswordAuthenticationToken basata sui dettagli dell'utente
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // Imposta l'oggetto Authentication nel SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Prosegue con la catena di filtri
        filterChain.doFilter(request, response);
    }
}
