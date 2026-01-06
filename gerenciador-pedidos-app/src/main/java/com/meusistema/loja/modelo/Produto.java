package com.meusistema.loja.modelo;

public class Produto {
	
	 private String id;
	    private String nome;
	    private double preco;
	    private int quantidadeEmEstoque;

	    public Produto(String id, String nome, double preco, int quantidadeEmEstoque) {
	        this.id = id;
	        this.nome = nome;
	        this.preco = preco;
	        this.quantidadeEmEstoque = quantidadeEmEstoque;
	    }

	    // Getters
	    public String getId() { return id; }
	    public String getNome() { return nome; }
	    public double getPreco() { return preco; }
	    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }

	    // Setters (se necessário, para exemplo vamos manter simples)
	    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) {
	        this.quantidadeEmEstoque = quantidadeEmEstoque;
	    }
	
}
