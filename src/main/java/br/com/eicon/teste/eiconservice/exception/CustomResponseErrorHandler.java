package br.com.eicon.teste.eiconservice.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            System.out.println("Bad Request error detected. Details: " + extractErrorMessage(response));
        } else {
            super.handleError(response);
        }
    }

    private String extractErrorMessage(ClientHttpResponse response) throws IOException {
        return new String(response.getBody().readAllBytes());
    }
}
