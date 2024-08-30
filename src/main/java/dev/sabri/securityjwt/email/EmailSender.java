package dev.sabri.securityjwt.email;

public interface EmailSender {
    void send(String to, String email);
}
