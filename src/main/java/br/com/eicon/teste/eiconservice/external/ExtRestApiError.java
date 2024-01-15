package br.com.eicon.teste.eiconservice.external;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ExtRestApiError {
    private final int code;
    private final String status;
    private final LocalDateTime timestamp;
    private final String message;
    private final String reason;

    public ExtRestApiError(Exception exception, HttpStatus status, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.message = message;
        this.reason = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }
}
