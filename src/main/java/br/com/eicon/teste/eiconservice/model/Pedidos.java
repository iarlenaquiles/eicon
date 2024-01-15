package br.com.eicon.teste.eiconservice.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Pedidos")
public class Pedidos {

    @JacksonXmlProperty(localName = "Pedido")
    @JacksonXmlCData
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Pedido> pedidos;

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPEdidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}