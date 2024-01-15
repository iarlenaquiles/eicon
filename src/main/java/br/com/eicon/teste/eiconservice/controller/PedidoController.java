package br.com.eicon.teste.eiconservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.eicon.teste.eiconservice.constants.ApiLinks;
import br.com.eicon.teste.eiconservice.model.Pedido;
import br.com.eicon.teste.eiconservice.service.PedidoService;

@RestController
@RequestMapping(ApiLinks.PEDIDOS)
@Validated
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.findAll();
    }

    @PostMapping(ApiLinks.RECEBER)
    public ResponseEntity<String> receberPedido(@Valid @RequestBody Pedido pedido) {
        if (pedidoService.adicionarPedido(pedido)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Pedido recebido com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar o pedido.");
        }
    }

    @PostMapping(ApiLinks.IMPORTAR)
    public String importarPedido() {
       return pedidoService.importarPedidos();
    }

    @GetMapping(ApiLinks.CONSULTAR)
    public ResponseEntity<List<Pedido>> consultarPedidos(
            @RequestParam(required = false) Integer numeroPedido,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataCadastro) {

        List<Pedido> result = pedidoService.obterPedidos(numeroPedido, dataCadastro);
        return ResponseEntity.ok(result);
    }
}
