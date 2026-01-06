package com.meusistema.loja.repositorios;

import com.meusistema.loja.modelo.Pedido;

public interface RepositorioPedido {
	
	 void salvar(Pedido pedido);
	 Pedido buscarPorId(String id); // Apenas um exemplo, não usaremos no teste agora
	
}
