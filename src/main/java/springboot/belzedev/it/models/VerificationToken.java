package springboot.belzedev.it.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;

//    @Column(name = "expiry_date")
    private LocalDateTime expiry_date;

    public VerificationToken(){}

    public VerificationToken(String token, User user, LocalDateTime expiry_date) {
        this.token = token;
        this.user = user;
        this.expiry_date = expiry_date;
    }

    public VerificationToken(Long id, String token, User user, LocalDateTime expiry_date) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiry_date = expiry_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiry_date;
    }

    public void setExpiryDate(LocalDateTime expiry_datee) {
        this.expiry_date = expiry_datee;
    }
}
