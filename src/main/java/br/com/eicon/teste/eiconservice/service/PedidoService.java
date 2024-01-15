package br.com.eicon.teste.eiconservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import br.com.eicon.teste.eiconservice.model.Pedido;
import br.com.eicon.teste.eiconservice.repository.PedidoRepository;
import br.com.eicon.teste.eiconservice.utils.JsonConverter;
import br.com.eicon.teste.eiconservice.utils.ResourceContent;
import br.com.eicon.teste.eiconservice.utils.XmlConverter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private final List<Integer> clientesCadastrados = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    public List<Pedido> findAll() {
        return this.pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("PEdido not found with id " + id);
        });
    }

    public Boolean adicionarPedido(Pedido pedido) {
        List<Pedido> pedidos = new ArrayList<>(this.pedidoRepository.findAll());
        log.info("pedidos: {}", pedidos);

        if (pedidos.size() >= 10) {
            return false;
        }

        if (pedidos.stream().anyMatch(p -> p.getNumeroControle() == pedido.getNumeroControle())) {
            return false;
        }

        if (pedido.getQuantidade() == 0) {
            pedido.setQuantidade(1);
        }

        if (!clientesCadastrados.contains(pedido.getCodigoCliente())) {
            return false;
        }

        pedido.calcularValorTotal();

        pedidos.add(pedido);
        this.pedidoRepository.save(pedido);
        log.info("@#@## pedidos: {}", pedidos);
        return true;
    }

    public String adicionar(Pedido pedido) {
        List<Pedido> pedidos = new ArrayList<>(this.pedidoRepository.findAll());
        log.info("pedidos: {}", pedidos);

        if (pedidos.size() >= 10) {
            return "Nao deve ter mais do que 10 pedidos";
        }

        if (pedidos.stream().anyMatch(p -> p.getNumeroControle() == pedido.getNumeroControle())) {
            return "Numero de controle ja existe";
        }

        if (!clientesCadastrados.contains(pedido.getCodigoCliente())) {
            return "Cliente inválido";
        }

        pedido.calcularValorTotal();
        pedido.setQuantidade(pedido.getQuantidade() == 0 ? 1 : pedido.getQuantidade());
        pedidos.add(pedido);
        this.pedidoRepository.save(pedido);
        log.info("@#@## pedidos: {}", pedidos);
        return "Cliente adicionado";
    }

    public List<Pedido> obterPedidos(Integer numeroPedido, LocalDateTime dataCadastro) {
        List<Pedido> pedidos = new ArrayList<>(this.pedidoRepository.findAll());

        return pedidos.stream()
                .filter(pedido -> (numeroPedido == null || pedido.getNumeroControle().equals(numeroPedido)))
                .filter(pedido -> (dataCadastro == null || pedido.getDataCadastro().equals(dataCadastro)))
                .collect(Collectors.toList());
    }

    private boolean verificarNumeroControleExistente(Integer numeroControle) {
        log.info("Numero controle: {}", numeroControle);
        List<Pedido> pedidos = new ArrayList<>(this.pedidoRepository.findAll());

        return pedidos.stream().anyMatch(pedido -> pedido.getNumeroControle().equals(numeroControle));
    }

    private String manipulatePedidos(List<Pedido> pedidos) {
        if (pedidos.size() > 10)
            return "O arquivo é limitado com 10 pedidos";

        for (Pedido novoPedido : pedidos) {
            // Verificações e manipulações dos pedidos
            if (!verificarNumeroControleExistente(novoPedido.getNumeroControle())) {
                return this.adicionar(novoPedido);
            } else {
                return "Número de controle já cadastrado: " + novoPedido.getNumeroControle();
            }
        }
        return null;
    }

    public String importarPedidos() {
        try {
            // Caminho do arquivo no diretório "resources" do projeto
            String filePath = "classpath:itens.json";

            // Carregar o recurso
            Resource resource = resourceLoader.getResource(filePath);

            // Ler o conteúdo do arquivo
            String fileContent = ResourceContent.readResourceContent(resource);

            if (filePath.contains(".json")) {
                List<Pedido> pedidos = JsonConverter.jsonToList(fileContent);
                return this.manipulatePedidos(pedidos);
            } else {
                List<Pedido> pedidosEmXml = XmlConverter.xmlToList(fileContent);
                log.info("pedidos em xml:{}", pedidosEmXml);
                return this.manipulatePedidos(pedidosEmXml);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
