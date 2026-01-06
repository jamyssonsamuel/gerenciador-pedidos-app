package com.meusistema.loja.servicos;
/**
 * Classe responsável por simular o cálculo do valor total de um pedido.
 * Contém um erro de lógica para fins de demonstração da depuração.
 */
public class CalculadoraPedidos {
	 /**
     * Calcula o total de um item após a aplicação do desconto.
     * * @param precoUnitario O preço de uma única unidade.
     * @param quantidade A quantidade de itens no pedido.
     * @param descontoPercentual O percentual de desconto (ex: 10.0 para 10%).
     * @return O valor total a ser pago (COM BUG).
     */
    
	public static double calcularTotal(double precoUnitario, int quantidade, double descontoPercentual) {
        
        // 1. Cálculo do subtotal
        double subtotal = precoUnitario * quantidade; 
        
        
        // 2. Cálculo do valor exato do desconto
        double valorDesconto = subtotal * (descontoPercentual / 100); 
        
        
        // 3. A_Linha do BUG: o programa_está SOMANDO o valorDesconto em_vez_de SUBTRAIR.
        // O valor_correto_esperado_para (100.0, 2, 10.0) é 180.0,_mas_este_código_retorna 220.0 
        
        return subtotal - valorDesconto;
    }

    /**
     * Método main simples para executar a classe e testar o cálculo diretamente.
     * É o ponto de entrada para iniciar a depuração (Debug As -> Java Application).
     */
    public static void main(String[] args) {
        
        // Teste de caso: R$ 100.00 x 2 unidades com 10% de desconto.
        // Resultado esperado: R$ 180.00
        
        double precoUnitario = 100.0;
        int quantidade = 2;
        double descontoPercentual = 10.0;
        
        double totalCalculado = calcularTotal(precoUnitario, quantidade, descontoPercentual);
        
        System.out.println("--- Teste de Calculo de Pedidos (Com Bug) ---");
        System.out.println("Preco Unitario: " + precoUnitario);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Desconto Aplicado: " + descontoPercentual + "%");
        System.out.println("---------------------------------------------");
        System.out.println("Total Calculado: R$ " + totalCalculado);
      
    }
}
