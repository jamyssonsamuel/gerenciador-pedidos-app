package com.meusistema.loja.servicos;

import com.meusistema.loja.modelo.Pedido;

public interface ServicoNotificacao {
	
	void enviarEmailConfirmacao(Pedido pedido);
	
}
