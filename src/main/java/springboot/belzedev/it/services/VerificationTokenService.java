package springboot.belzedev.it.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.belzedev.it.models.User;
import springboot.belzedev.it.models.VerificationToken;
import springboot.belzedev.it.repository.UserRepository;
import springboot.belzedev.it.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public void createVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user, LocalDateTime.now().plusHours(1));
        tokenRepository.save(verificationToken);
    }

    public String getTokenForUser(User user) {
        return tokenRepository.findByUser(user)
                .map(VerificationToken::getToken)
                .orElseThrow(() -> new RuntimeException("No token found for user"));
    }

    public boolean validateVerificationToken(String token){
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(token);
        if(optionalToken.isPresent()){
            VerificationToken verificationToken = optionalToken.get();
            if(verificationToken.getExpiryDate().isAfter(LocalDateTime.now())){
                User user = verificationToken.getUser();
                user.setVerified(true);
                userRepository.save(user);
                tokenRepository.delete(verificationToken);
                return true;
            }
        }
        return false;
    }
}
