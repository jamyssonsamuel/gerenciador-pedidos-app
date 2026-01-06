package com.meusistema.loja.modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
	
	private String id;
    private String clienteEmail;
    private List<ItemPedido> itens;
    private double valorTotal;
    private String status; // Ex: PENDENTE, PROCESSADO, CANCELADO

    public Pedido(String id, String clienteEmail) {
        this.id = id;
        this.clienteEmail = clienteEmail;
        this.itens = new ArrayList<>();
        this.status = "PENDENTE";
    }

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        calcularValorTotal(); // Recalcula o total sempre que um item é adicionado
    }

    private void calcularValorTotal() {
        this.valorTotal = itens.stream()
                               .mapToDouble(ItemPedido::getSubtotal)
                               .sum();
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getClienteEmail() { return clienteEmail; }
    public List<ItemPedido> getItens() { return itens; }
    public double getValorTotal() { return valorTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; } // Apenas para mock, normalmente seria calculado
	
}
