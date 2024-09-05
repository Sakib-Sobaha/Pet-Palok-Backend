package dev.sabri.securityjwt.response;



import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthResponse {
    private String token;
    private long expiresIn;

    public AuthResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

}