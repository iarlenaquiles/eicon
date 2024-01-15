package br.com.eicon.teste.eiconservice.exception;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.eicon.teste.eiconservice.constants.ApiExceptionMessages;
import br.com.eicon.teste.eiconservice.external.ExtRestApiError;

@Order(HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.error(ApiExceptionMessages.PARSE_THE_QUERYSTRING_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.PARSE_THE_QUERYSTRING_MESSAGE),
                new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        LOG.error(ApiExceptionMessages.PARSE_THE_HTTP_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.PARSE_THE_HTTP_MESSAGE),
                new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        LOG.error(ApiExceptionMessages.EXECUTE_URL_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.EXECUTE_URL_MESSAGE),
                new HttpHeaders(), status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExtRestApiError> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        LOG.error(ApiExceptionMessages.ILLEGAL_ARGUMENT_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.ILLEGAL_ARGUMENT_MESSAGE),
                new HttpHeaders(), status);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ExtRestApiError> handleInterruptedException(InterruptedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        LOG.error(ApiExceptionMessages.INTERRUPTED_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.INTERRUPTED_MESSAGE),
                new HttpHeaders(), status);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ExtRestApiError> handleTimeoutException(TimeoutException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        LOG.error(ApiExceptionMessages.INTERRUPTED_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.INTERRUPTED_MESSAGE),
                new HttpHeaders(), status);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExtRestApiError> handleIOException(IOException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        LOG.error(ApiExceptionMessages.INTERRUPTED_MESSAGE, ex);
        return new ResponseEntity<>(new ExtRestApiError(ex, status, ApiExceptionMessages.INTERRUPTED_MESSAGE),
                new HttpHeaders(), status);
    }
}
