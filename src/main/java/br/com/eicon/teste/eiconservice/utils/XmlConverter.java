package br.com.eicon.teste.eiconservice.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.eicon.teste.eiconservice.model.Pedido;
import br.com.eicon.teste.eiconservice.model.Pedidos;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlConverter {
    public static List<Pedido> xmlToList(String xmlString) throws IOException {
        log.info(xmlString);
        XmlMapper xmlMapper = new XmlMapper();
        Pedidos pedidos = xmlMapper.readValue(xmlString, Pedidos.class);
        log.info("Pedidos no xml converter: {}", pedidos);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = mapper.writeValueAsString(pedidos);
        return JsonConverter.jsonToList(json);
    }
}
