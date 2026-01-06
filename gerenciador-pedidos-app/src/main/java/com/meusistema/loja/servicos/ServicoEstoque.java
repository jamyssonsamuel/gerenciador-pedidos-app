package com.meusistema.loja.servicos;

import com.meusistema.loja.modelo.Produto;

public interface ServicoEstoque {
	
	 boolean verificarDisponibilidade(String produtoId, int quantidade);
	    void baixarEstoque(String produtoId, int quantidade);
	    Produto buscarProdutoPorId(String produtoId); // Para obter preço
	
}
