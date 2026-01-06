package com.meusistema.loja.servicos;

import com.meusistema.loja.modelo.ItemPedido;
import com.meusistema.loja.modelo.Pedido;
import com.meusistema.loja.modelo.Produto;
import com.meusistema.loja.repositorios.RepositorioPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // Importa os métodos estáticos do Mockito

@ExtendWith(MockitoExtension.class) // Habilita as anotações do Mockito para JUnit 5
class GerenciadorDePedidosTest {

    @Mock // Mockito criará um mock para esta interface
    private ServicoEstoque servicoEstoque;

    @Mock // Mockito criará um mock para esta interface
    private RepositorioPedido repositorioPedido;

    @Mock // Mockito criará um mock para esta interface
    private ServicoNotificacao servicoNotificacao;

    @InjectMocks // Injeta os mocks acima nesta instância de GerenciadorDePedidos
    private GerenciadorDePedidos gerenciadorDePedidos;

    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setup() {
        // Inicializa produtos de exemplo para os testes
        produto1 = new Produto("P001", "Laptop Gamer", 5000.00, 10);
        produto2 = new Produto("P002", "Mouse Sem Fio", 150.00, 50);

        // Nenhuma ação aqui, pois @InjectMocks e @Mock já fazem a injeção.
        // Se não usasse @InjectMocks, faria:
        // gerenciadorDePedidos = new GerenciadorDePedidos(servicoEstoque, repositorioPedido, servicoNotificacao);
    }

    // --- Teste 1: Processar pedido com sucesso ---
    @Test
    void deveProcessarPedidoComSucesso() {
        // ARRANGE (Configurar os Mocks)
        ItemPedido item1 = new ItemPedido(new Produto("P001", "Laptop Gamer", 0, 0), 1); // Preço e estoque iniciais não importam aqui
        ItemPedido item2 = new ItemPedido(new Produto("P002", "Mouse Sem Fio", 0, 0), 2);
        List<ItemPedido> itens = Arrays.asList(item1, item2);

        // Definir o comportamento dos mocks (o que eles devem retornar quando chamados)
        when(servicoEstoque.buscarProdutoPorId("P001")).thenReturn(produto1);
        when(servicoEstoque.buscarProdutoPorId("P002")).thenReturn(produto2);
        when(servicoEstoque.verificarDisponibilidade("P001", 1)).thenReturn(true);
        when(servicoEstoque.verificarDisponibilidade("P002", 2)).thenReturn(true);

        // ACT (Executar o método a ser testado)
        Pedido pedidoProcessado = gerenciadorDePedidos.processarNovoPedido("cliente@email.com", itens);

        // ASSERT (Verificar resultados e interações)
        assertNotNull(pedidoProcessado);
        assertEquals("PROCESSADO", pedidoProcessado.getStatus());
        assertEquals(produto1.getPreco() * 1 + produto2.getPreco() * 2, pedidoProcessado.getValorTotal(), 0.001); // 5000*1 + 150*2 = 5300

        // Verificar se os métodos dos mocks foram chamados como esperado
        verify(servicoEstoque, times(1)).baixarEstoque("P001", 1);
        verify(servicoEstoque, times(1)).baixarEstoque("P002", 2);
        verify(repositorioPedido, times(1)).salvar(pedidoProcessado); // Verifica se o pedido foi salvo
        verify(servicoNotificacao, times(1)).enviarEmailConfirmacao(pedidoProcessado); // Verifica se a notificação foi enviada
    }

    // --- Teste 2: Falha por estoque insuficiente ---
    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        // ARRANGE
        ItemPedido item1 = new ItemPedido(new Produto("P001", "Laptop Gamer", 0, 0), 1);
        List<ItemPedido> itens = Collections.singletonList(item1);

        when(servicoEstoque.buscarProdutoPorId("P001")).thenReturn(produto1);
        // Simulamos que a disponibilidade é falsa para este teste
        when(servicoEstoque.verificarDisponibilidade("P001", 1)).thenReturn(false);

        // ACT & ASSERT (Esperamos uma exceção)
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gerenciadorDePedidos.processarNovoPedido("cliente@email.com", itens);
        });

        // Verificar a mensagem da exceção
        assertTrue(exception.getMessage().contains("Estoque insuficiente"));

        // Verificar que nenhum método de alteração de estado ou notificação foi chamado
        verify(servicoEstoque, never()).baixarEstoque(anyString(), anyInt()); // Nenhuma baixa de estoque
        verify(repositorioPedido, never()).salvar(any(Pedido.class)); // Nenhum pedido salvo
        verify(servicoNotificacao, never()).enviarEmailConfirmacao(any(Pedido.class)); // Nenhuma notificação enviada
    }

    // --- Teste 3: Pedido vazio ---
    @Test
    void deveLancarExcecaoQuandoPedidoVazio() {
        // ARRANGE
        List<ItemPedido> itensVazios = Collections.emptyList();

        // ACT & ASSERT
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciadorDePedidos.processarNovoPedido("cliente@email.com", itensVazios);
        });

        assertTrue(exception.getMessage().contains("O pedido deve conter itens."));

        // Nenhuma interação com mocks esperada
        verifyNoInteractions(servicoEstoque, repositorioPedido, servicoNotificacao);
    }

    // --- Teste 4: Produto não encontrado ---
    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // ARRANGE
        ItemPedido item1 = new ItemPedido(new Produto("PROD_INEXISTENTE", "Prod X", 0, 0), 1);
        List<ItemPedido> itens = Collections.singletonList(item1);

        // Simula que o produto não é encontrado
        when(servicoEstoque.buscarProdutoPorId("PROD_INEXISTENTE")).thenReturn(null);

        // ACT & ASSERT
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            gerenciadorDePedidos.processarNovoPedido("cliente@email.com", itens);
        });

        assertTrue(exception.getMessage().contains("Produto com ID PROD_INEXISTENTE não encontrado."));

        // Nenhuma interação de alteração de estado esperada
        verify(servicoEstoque, never()).baixarEstoque(anyString(), anyInt());
        verify(repositorioPedido, never()).salvar(any(Pedido.class));
        verify(servicoNotificacao, never()).enviarEmailConfirmacao(any(Pedido.class));
    }
}