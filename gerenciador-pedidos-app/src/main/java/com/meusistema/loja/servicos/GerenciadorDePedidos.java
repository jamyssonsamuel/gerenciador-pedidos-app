package com.meusistema.loja.servicos;

import java.util.List;

import com.meusistema.loja.modelo.Produto;
import com.meusistema.loja.modelo.ItemPedido;
import com.meusistema.loja.modelo.Pedido;
import com.meusistema.loja.repositorios.RepositorioPedido;

public class GerenciadorDePedidos {
	
	private ServicoEstoque servicoEstoque;
    private RepositorioPedido repositorioPedido;
    private ServicoNotificacao servicoNotificacao;

    // Injeção de Dependência via Construtor - MELHOR PRÁTICA para testabilidade
    public GerenciadorDePedidos(ServicoEstoque servicoEstoque,
                                RepositorioPedido repositorioPedido,
                                ServicoNotificacao servicoNotificacao) {
        this.servicoEstoque = servicoEstoque;
        this.repositorioPedido = repositorioPedido;
        this.servicoNotificacao = servicoNotificacao;
    }

    public Pedido processarNovoPedido(String clienteEmail, List<ItemPedido> itensDesejados) {
        if (itensDesejados == null || itensDesejados.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter itens.");
        }

        Pedido novoPedido = new Pedido("PEDIDO_" + System.currentTimeMillis(), clienteEmail);
        double valorTotalCalculado = 0;

        // 1. Validar estoque e calcular preço
        for (ItemPedido item : itensDesejados) {
            Produto produtoNoCatalogo = servicoEstoque.buscarProdutoPorId(item.getProduto().getId());

            if (produtoNoCatalogo == null) {
                throw new IllegalStateException("Produto com ID " + item.getProduto().getId() + " não encontrado.");
            }

            // Usamos o preço do produto retornado pelo serviço de estoque
            // para garantir que o preço usado no pedido é o preço atual do sistema
            Produto produtoComPrecoAtual = new Produto(
                produtoNoCatalogo.getId(),
                produtoNoCatalogo.getNome(),
                produtoNoCatalogo.getPreco(), // PREÇO ATUAL DO ESTOQUE
                produtoNoCatalogo.getQuantidadeEmEstoque()
            );
            ItemPedido itemComPrecoAtualizado = new ItemPedido(produtoComPrecoAtual, item.getQuantidade());


            if (!servicoEstoque.verificarDisponibilidade(itemComPrecoAtualizado.getProduto().getId(), itemComPrecoAtualizado.getQuantidade())) {
                throw new IllegalStateException("Estoque insuficiente para o produto: " + itemComPrecoAtualizado.getProduto().getNome());
            }
            novoPedido.adicionarItem(itemComPrecoAtualizado); // Adiciona o item com preço atualizado
            valorTotalCalculado += itemComPrecoAtualizado.getSubtotal();
        }

        // Definir o valor total do pedido após adicionar todos os itens
        novoPedido.setValorTotal(valorTotalCalculado);


        // 2. Baixar estoque
        for (ItemPedido item : novoPedido.getItens()) {
            servicoEstoque.baixarEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        // 3. Salvar pedido
        repositorioPedido.salvar(novoPedido);
        novoPedido.setStatus("PROCESSADO"); // Altera o status após salvar

        // 4. Enviar notificação
        servicoNotificacao.enviarEmailConfirmacao(novoPedido);

        return novoPedido;
    }
	
}
