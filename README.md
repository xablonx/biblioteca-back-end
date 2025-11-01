🚀 Backend - Guia de Execução (Spring Boot)

📖 Visão Geral

Este repositório contém o Backend da aplicação, desenvolvido com Spring Boot 3.x e Java 21. A aplicação utiliza Maven para gerenciamento de dependências e integrações externas.

📋 Pré-requisitos
Certifique-se de que os seguintes itens estejam instalados e configurados em seu ambiente:
- Java Development Kit (JDK): Versão 21 ou superior.
- Gerenciador de Dependências: Apache Maven.

⚙️ Configuração do Ambiente.

Banco de Dados (H2 em Memória)

O arquivo application.properties indica que a aplicação está configurada para utilizar o H2 Database em memória em modo de desenvolvimento. 
Não será necessário configurar um banco de dados externo. O Spring criará e destruirá o banco automaticamente a cada execução.
 
Acesso ao Console do H2: 

Se necessário inspecionar os dados durante a execução:
O console do H2 é ativado automaticamente. Acesse: http://localhost:8080/h2-console/
Use as credenciais no arquivo application.properties para logar (spring.datasource.username e spring.datasource.password).

▶️ Como Executar o Projeto
O Backend será executado na porta padrão 8080.

Opção 1: Via Linha de Comando (Maven)

Navegue até o diretório raiz do projeto (onde o pom.xml está localizado).
Compile e empacote o projeto:

Bash -> mvn clean install

Execute a aplicação usando o plugin Spring Boot:
Bash-> mvn spring-boot:run

Opção 2: Via IDE (IntelliJ/VSCode)

Abra o projeto na sua IDE.
Localize a classe principal da aplicação (BibliotecaBackEndApplication.class).
Execute a classe (botão ▶️ Run).

O servidor estará rodando em http://localhost:8080. Se você estiver usando o Frontend Angular, ele se conectará automaticamente via proxy a esta porta.


🌐 Referência da API REST

A API está acessível na porta `8080`. O prefixo base para todas as rotas é **`/api`**.

Endpoints

| Recurso | Método HTTP | Rota | Descrição |
| :--- | :--- | :--- | :--- |
| **Usuários** | `GET` | `/api/usuarios` | Lista todos os usuários cadastrados. |
| | `GET` | `/api/usuarios/{id}` | Busca um usuário por ID. |
| | `POST` | `/api/usuarios` | Cria um novo usuário. |
| | `PUT` | `/api/usuarios/{id}` | Atualiza um usuário por ID. |
| | `DELETE` | `/api/usuarios/{id}` | Remove um usuário por ID. |
| **Livros (CRUD)** | `GET` | `/api/livros` | Lista todos os livros no acervo. |
| | `GET` | `/api/livros/{id}` | Busca um livro por ID. |
| | `POST` | `/api/livros` | Cadastra um novo livro. |
| | `PUT` | `/api/livros/{id}` | Atualiza um livro por ID. |
| | `DELETE` | `/api/livros/{id}` | Remove um livro por ID. |
| **Livros (Externo)** | `GET` | `/api/livros/google-search?titulo={titulo}` | Pesquisa livros na API do Google Books. |
| | `POST` | `/api/livros/google-save` | Salva um livro retornado do Google Books no acervo local. |
| **Empréstimos** | `GET` | `/api/emprestimos` | Lista todos os empréstimos ativos e finalizados. |
| | `POST` | `/api/emprestimos` | Cria um novo empréstimo. |
| | `PATCH` | `/api/emprestimos/{id}/devolucao` | Registra a devolução de um livro (finaliza o empréstimo). |
| **Recomendações** | `GET` | `/api/recomendacoes/usuarios/{usuarioId}` | Retorna livros recomendados com base no histórico do usuário. |

Exemplos de requisições no arquivo: http-request.http