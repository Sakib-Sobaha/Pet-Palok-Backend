package dev.sabri.securityjwt.system.exception;

public class CustomBlobStorageException extends RuntimeException {
    public CustomBlobStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
