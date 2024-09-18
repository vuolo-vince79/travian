package springboot.belzedev.it.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.belzedev.it.dto.ApiResponse;
import springboot.belzedev.it.services.VerificationTokenService;

import java.net.URI;

@RestController
@RequestMapping("/api/verify-email")
public class VerificationTokenController {

    @Autowired
    private VerificationTokenService tokenService;

//    @GetMapping
//    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token){
//        boolean isVerified = tokenService.validateVerificationToken(token);
//        if (isVerified) {
//            return ResponseEntity.ok(new ApiResponse("Email verificata con successo", true));
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse("Token di verifica non valido o scaduto", false));
//        }
//    }

//    @GetMapping
//    public String verifyEmail(@RequestParam("token") String token){
//        boolean isVerified = tokenService.validateVerificationToken(token);
//        if (isVerified) {
//            return "redirect:http://localhost:4200/login?verified=true";
//        } else {
//            return "redirect:http://localhost:4200/login?verified=false";
//        }
//    }

    @GetMapping
    public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token){
        boolean isVerified = tokenService.validateVerificationToken(token);
        String redirect = "http://localhost:4200/#/login?verified=" + (isVerified ? "true" : "false");

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirect))
                .build();
    }

}
