# Gerenciador de Pedidos App 📦

Este projeto foi desenvolvido como parte prática da disciplina de **Teste de Software** no curso de **Técnico em Informática do IFPE - Campus Garanhuns**. O foco principal não é apenas a funcionalidade de negócio, mas a garantia da qualidade através de testes automatizados.

## 📝 Do que se trata o projeto?
O sistema simula o fluxo básico de um gerenciador de pedidos de uma loja online. Ele permite o cadastro de itens, cálculo de descontos e o processamento de pagamentos. 

A grande "estratégia" aqui foi aplicar os conceitos de **Testes Unitários**. O projeto serve para demonstrar como validar regras de negócio isoladas, garantindo que mudanças futuras no código não quebrem funcionalidades que já estavam funcionando.

## 🛠️ Detalhes Técnicos
Para este projeto, utilizei uma stack moderna de desenvolvimento Java voltada para testes:

* **Linguagem:** Java 17
* **Gerenciador de Dependências:** Maven
* **Framework de Testes:** [JUnit 5 (Jupiter)](https://junit.org/junit5/) - Utilizado para a estrutura dos casos de teste e asserções.
* **Biblioteca de Mocking:** [Mockito](https://site.mockito.org/) - Utilizado para criar objetos simulados (Mocks), permitindo testar a lógica de um serviço sem precisar acessar um banco de dados real ou serviços externos.
* **IDE recomendada:** Eclipse IDE.

## ▶️ Detalhes de Execução
Para rodar o projeto e verificar os testes em sua máquina:

### Pré-requisitos
* Java JDK 17 ou superior instalado.
* Maven configurado no seu sistema (ou utilize o plugin do Maven na sua IDE).

### Passo a Passo
1.  **Clonar o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/gerenciador-pedidos-app.git](https://github.com/seu-usuario/gerenciador-pedidos-app.git)
    ```
2.  **Importar na IDE:**
    * No Eclipse: `File` > `Import` > `Existing Maven Projects`.
3.  **Executar os testes via Terminal:**
    ```bash
    mvn test
    ```
4.  **Executar os testes na IDE:**
    * Clique com o botão direito na pasta `src/test/java` > `Run As` > `JUnit Test`.

---
Desenvolvido por **Jâmysson Samuel** como exercício acadêmico no IFPE Garanhuns.
