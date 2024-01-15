package br.com.eicon.teste.eiconservice.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eicon.teste.eiconservice.model.Pedido;

public class JsonConverter {

    public static List<Pedido> jsonToList(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<Pedido>>() {
        });
    }
}