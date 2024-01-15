package br.com.eicon.teste.eiconservice.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

public class JsonUtils {
    public static boolean isJson(String content) {
        try {
            new ObjectMapper().readTree(content);
            return true;
        } catch (MismatchedInputException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException("Error checking if the content is JSON.", e);
        }
    }
}
