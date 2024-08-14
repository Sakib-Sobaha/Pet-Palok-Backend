package dev.sabri.securityjwt.system.exception;


import dev.sabri.securityjwt.system.Result;
import dev.sabri.securityjwt.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(CustomBlobStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleCustomBlobStorageException(CustomBlobStorageException ex) {
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getCause().getMessage());
    }
}
