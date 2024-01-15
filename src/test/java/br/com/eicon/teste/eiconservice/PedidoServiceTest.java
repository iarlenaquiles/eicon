package br.com.eicon.teste.eiconservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import br.com.eicon.teste.eiconservice.model.Pedido;
import br.com.eicon.teste.eiconservice.repository.PedidoRepository;
import br.com.eicon.teste.eiconservice.service.PedidoService;

@SpringBootTest
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private Logger log;

    @Mock
    private ResourceLoader resourceLoader;

    @Test
    void testCalcularDesconto() {
        // Crie um pedido
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste");
        pedido.setQuantidade(100);
        pedido.setValor(10.0);

        pedido.calcularValorTotal();

        assertEquals(900.0, pedido.getValorTotal());
    }

    @Test
    void testCalcularDesconto_QuantidadeMaiorQueDez() {
        // Crie um pedido
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(60);
        pedido.setValor(10.0);

        pedido.calcularValorTotal();

        assertEquals(570.0, pedido.getValorTotal());
    }

    @Test
    void testCalcularDesconto_QuantidadeMaiorQueCinco() {
        // Crie um pedido
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(6);
        pedido.setValor(10.0);

        pedido.calcularValorTotal();

        assertEquals(57.0, pedido.getValorTotal());
    }

    @Test
    void testFindAll() {
        // Crie alguns pedidos para retornar do repositório
        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setCodigoCliente(1);
        pedido1.setNome("Teste 1");
        pedido1.setQuantidade(6);
        pedido1.setValor(10.0);
        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        pedido2.setCodigoCliente(1);
        pedido2.setNome("Teste 2");
        pedido2.setQuantidade(60);
        pedido2.setValor(10.0);
        List<Pedido> pedidosFicticios = Arrays.asList(pedido1, pedido2);

        when(pedidoRepository.findAll()).thenReturn(pedidosFicticios);

        List<Pedido> resultado = pedidoService.findAll();

        verify(pedidoRepository, times(1)).findAll();

        assertEquals(pedidosFicticios, resultado);
    }

    @Test
    void testFindById() {
        // Crie um ID para o pedido
        Long idPedido = 1L;

        // Crie um pedido para retornar do repositório
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(6);
        pedido.setValor(10.0);

        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.findById(idPedido);

        verify(pedidoRepository, times(1)).findById(idPedido);

        // Verifique se o resultado do serviço é o mesmo que o esperado
        assertEquals(pedido, resultado);
    }

    @Test
    void testAdicionarClienteValido() {
        List<Pedido> existingPedidos = new ArrayList<>();
        when(pedidoRepository.findAll()).thenReturn(existingPedidos);
        when(log.isInfoEnabled()).thenReturn(true);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(6);
        pedido.setValor(10.0);

        String result = pedidoService.adicionar(pedido);

        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, times(1)).save(pedido);

        assertEquals("Cliente adicionado", result);
    }

    @Test
    void testAdicionarMaisDeDezPedidos() {
        List<Pedido> existingPedidos = new ArrayList<>(Arrays.asList(new Pedido(), new Pedido(), new Pedido(),
                new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido(), new Pedido()));
        when(pedidoRepository.findAll()).thenReturn(existingPedidos);
        when(log.isInfoEnabled()).thenReturn(true);

        Pedido pedido = new Pedido();

        // Call the method to be tested
        String result = pedidoService.adicionar(pedido);

        // Verify the behavior and result
        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, never()).save(pedido);
        verify(log, times(1)).info("pedidos: {}", existingPedidos);

        assertEquals("Nao deve ter mais do que 10 pedidos", result);
    }

    @Test
    void testAdicionarNumeroControleExistente() {
        Pedido existingPedido = new Pedido();
        existingPedido.setId(1L);
        existingPedido.setCodigoCliente(1);
        existingPedido.setNome("Teste 2");
        existingPedido.setQuantidade(6);
        existingPedido.setValor(10.0);

        List<Pedido> existingPedidos = new ArrayList<>(Arrays.asList(existingPedido));
        when(pedidoRepository.findAll()).thenReturn(existingPedidos);
        when(log.isInfoEnabled()).thenReturn(true);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(1);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(6);
        pedido.setValor(10.0);

        String result = pedidoService.adicionar(pedido);

        // Verify the behavior and result
        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, never()).save(pedido);
        verify(log, times(1)).info("pedidos: {}", existingPedidos);

        assertEquals("Numero de controle ja existe", result);
    }

    @Test
    void testAdicionarClienteInvalido() {
        List<Pedido> existingPedidos = new ArrayList<>();
        when(pedidoRepository.findAll()).thenReturn(existingPedidos);
        when(log.isInfoEnabled()).thenReturn(true);

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCodigoCliente(100);
        pedido.setNome("Teste 2");
        pedido.setQuantidade(6);
        pedido.setValor(10.0);
        // Call the method to be tested
        String result = pedidoService.adicionar(pedido);

        // Verify the behavior and result
        verify(pedidoRepository, times(1)).findAll();
        verify(pedidoRepository, never()).save(pedido);
        verify(log, times(1)).info("pedidos: {}", existingPedidos);

        assertEquals("Cliente inválido", result);
    }

    @Test
    void testObterPedidosByNumeroPedidoAndDataCadastro() {
        List<Pedido> allPedidos = Arrays.asList(
                new Pedido(),
                new Pedido(),
                new Pedido());
        when(pedidoRepository.findAll()).thenReturn(allPedidos);

        List<Pedido> result = pedidoService.obterPedidos(2, LocalDateTime.now());

        verify(pedidoRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getNumeroControle());
    }

    @Test
    void testObterPedidosByNumeroPedidoOnly() {
        // Mock the data for the repository
        List<Pedido> allPedidos = Arrays.asList(
                new Pedido(),
                new Pedido(),
                new Pedido());
        when(pedidoRepository.findAll()).thenReturn(allPedidos);

        // Call the method to be tested
        List<Pedido> result = pedidoService.obterPedidos(2, null);

        // Verify the behavior and result
        verify(pedidoRepository, times(1)).findAll();
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getNumeroControle());
    }

    @Test
    void testObterPedidosByDataCadastroOnly() {
        // Mock the data for the repository
        List<Pedido> allPedidos = Arrays.asList(
                new Pedido(),
                new Pedido(),
                new Pedido());
        when(pedidoRepository.findAll()).thenReturn(allPedidos);

        // Call the method to be tested
        List<Pedido> result = pedidoService.obterPedidos(null, LocalDateTime.now());

        // Verify the behavior and result
        verify(pedidoRepository, times(1)).findAll();
        assertEquals(3, result.size()); // All pedidos should be returned
    }

    @Test
    void testObterPedidosWithNoFilters() {
        // Mock the data for the repository
        List<Pedido> allPedidos = Arrays.asList(
                new Pedido(),
                new Pedido(),
                new Pedido());
        when(pedidoRepository.findAll()).thenReturn(allPedidos);

        List<Pedido> result = pedidoService.obterPedidos(null, null);

        verify(pedidoRepository, times(1)).findAll();
        assertEquals(3, result.size());
    }

    @Test
    void testImportarPedidosJson() throws IOException {
        // Mock the resource and file content
        Resource resource = new ClassPathResource("itens.json");
        String fileContent = "[{\"numeroControle\":1,\"nome\":\"Produto1\",\"valor\":10.0,\"quantidade\":5,\"codigoCliente\":1}]";
        when(resourceLoader.getResource("classpath:itens.json")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(IOUtils.toInputStream(fileContent, StandardCharsets.UTF_8));

        // Call the method to be tested
        String result = pedidoService.importarPedidos();

        // Verify the behavior and result
        assertNotNull(result);
        assertTrue(result.contains("Pedido adicionado"));
    }

    @Test
    void testImportarPedidosXml() throws IOException {
        // Mock the resource and file content
        Resource resource = new ClassPathResource("itens.xml");
        String fileContent = "<Pedidos><Pedido><numeroControle>1</numeroControle><nome>Produto1</nome><valor>10.0</valor><quantidade>5</quantidade><codigoCliente>1</codigoCliente></Pedido></Pedidos>";
        when(resourceLoader.getResource("classpath:itens.xml")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(IOUtils.toInputStream(fileContent, StandardCharsets.UTF_8));

        // Call the method to be tested
        String result = pedidoService.importarPedidos();

        // Verify the behavior and result
        assertNotNull(result);
        assertTrue(result.contains("Pedido adicionado"));
    }

    @Test
    void testImportarPedidosIOException() throws IOException {
        Resource resource = mock(Resource.class);
        when(resourceLoader.getResource("classpath:itens.json")).thenReturn(resource);
        when(resource.getInputStream()).thenThrow(new IOException());

        String result = pedidoService.importarPedidos();

        assertNull(result);
    }
}
