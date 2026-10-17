# InvestTrack — Sistema de Gerenciamento de Carteira de Investimentos


## 2. Descrição do projeto

O InvestTrack é uma aplicação desenvolvida em Java para gerenciamento de carteiras de investimentos em ações. O sistema permite cadastrar ativos, consultar e editar informações, realizar compras e vendas, acompanhar o saldo disponível e visualizar informações sobre o patrimônio e o desempenho da carteira.


## 3. Problema que o sistema busca resolver

O InvestTrack busca centralizar essas informações em uma aplicação simples, facilitando o acompanhamento das movimentações financeiras e a consulta dos principais indicadores da carteira.


## 4. Funcionalidades

O sistema oferece as seguintes funcionalidades:

1. **Cadastro de ativos:** registra ações com ticker, nome, setor e preço atual.
2. **Listagem de ativos:** apresenta os ativos cadastrados.
3. **Busca de ativos:** permite localizar um ativo pelo ticker.
4. **Edição de ativos:** permite atualizar informações de um ativo cadastrado.
5. **Desativação de ativos:** impede novas negociações de ativos desativados, preservando seus registros.
6. **Compra de ações:** registra quantidade, preço unitário e taxa da operação, atualizando o saldo e a posição.
7. **Venda de ações:** registra vendas, atualiza o saldo e reduz a quantidade mantida na carteira.
8. **Consulta da carteira:** apresenta saldo disponível, valores investidos e patrimônio.
9. **Histórico de operações:** permite consultar compras e vendas registradas.
10. **Relatórios gerenciais:** apresenta indicadores e um ranking das posições por resultado financeiro.
11. **Exportação de dados:** permite salvar informações em arquivos CSV.

## 5. Tecnologias utilizadas

- **Java:** linguagem de programação principal.
- **JDK:** ambiente de desenvolvimento e execução Java.
- **Maven:** gerenciamento do projeto, dependências, compilação e execução.
- **JUnit 5:** criação e execução de testes automatizados.
- **CSV:** formato utilizado para persistência e exportação dos dados.
- **Git:** controle de versão do código-fonte.

## 6. Estrutura das classes

O projeto utiliza as seguintes classes principais:

### 6.1. Ativo

Representa um ativo financeiro cadastrado no sistema.

Principais responsabilidades:
- Armazenar ticker, nome, setor e preço atual.
- Controlar se o ativo está ativo ou desativado.
- Atualizar o preço.
- Registrar cotações em seu histórico.

### 6.2. Cotacao

Representa uma cotação de um ativo em determinada data.

Armazena o preço e a data correspondente, permitindo organizar o histórico de cotações.

### 6.3. Investidor

Representa o investidor associado à carteira.

Armazena informações como nome e e-mail.

### 6.4. Posicao

Representa a quantidade de um ativo mantida na carteira.

Responsabilidades:
- Controlar a quantidade de ações.
- Calcular o custo médio.
- Calcular o total investido.
- Calcular o valor atual da posição.
- Calcular o lucro ou prejuízo e a rentabilidade percentual.
- Atualizar a posição após compras e vendas.

### 6.5. Operacao

Representa uma compra ou venda realizada.

Armazena o tipo de operação, o ativo, a quantidade, o preço unitário, a taxa e a data. Também calcula o valor bruto e o valor total da operação.

### 6.6. Carteira

Representa a carteira de investimentos do investidor.

Mantém o saldo disponível, as posições e o histórico de operações. É responsável por executar compras e vendas, aplicar as validações financeiras e calcular os indicadores gerais da carteira.

### 6.7. App

É a classe principal da aplicação.

Responsável por iniciar o programa, apresentar o menu, receber os dados digitados pelo usuário, chamar os métodos das classes de negócio e realizar a leitura e gravação dos arquivos CSV.

## 7. Relacionamentos entre as classes

O sistema apresenta os seguintes relacionamentos:

- Um `Investidor` possui uma `Carteira`.
- Uma `Carteira` mantém uma coleção de `Posicao`.
- Cada `Posicao` está associada a um `Ativo`.
- Uma `Carteira` mantém uma coleção de `Operacao`.
- Cada `Operacao` referencia o `Ativo` negociado.
- Um `Ativo` mantém uma coleção de objetos `Cotacao`.
- A classe `App` coordena a interação entre o usuário e as demais classes.

As coleções são implementadas com `ArrayList`, permitindo armazenar e consultar múltiplos objetos.


## 8. Regras de negócio

-Determinadas pra consistência das operaçês em geral

1. O saldo inicial não pode ser negativo ou inválido.
2. Um ativo precisa estar cadastrado e ativo para permitir uma nova compra ou venda.
3. Uma compra não pode ser realizada quando o saldo disponível é insuficiente.
4. Uma venda não pode ultrapassar a quantidade disponível na posição.
5. A quantidade de ações negociadas deve ser válida e positiva.
6. Os preços e as taxas devem respeitar as validações definidas pelas classes, sem aceitar valores inválidos.
7. A taxa de uma venda não pode superar o valor bruto da operação.
8. O custo médio da posição é atualizado quando novas compras são realizadas.
9. Quando uma posição chega a zero ações, ela deixa de aparecer entre as posições abertas.
10. As operações registradas são mantidas no histórico e podem ser recuperadas a partir do arquivo CSV.
11. Ativos desativados não podem ser negociados em novas operações, mas seus registros são preservados.

## 9. Relatórios e indicadores

A aplicação disponibiliza indicadores para facilitar o acompanhamento da carteira:

- **Saldo disponível:** dinheiro restante após as movimentações.
- **Total investido:** soma do custo das posições abertas.
- **Patrimônio:** saldo disponível somado ao valor atual das posições.
- **Quantidade de posições:** número de posições abertas na carteira.
- **Quantidade de operações:** número de compras e vendas registradas.
- **Maior lucro ou resultado:** posição com o maior resultado financeiro calculado.
- **Menor lucro ou resultado:** posição com o menor resultado financeiro calculado.
- **Rentabilidade média:** média simples dos percentuais de rentabilidade das posições abertas.
- **Ranking de posições:** classificação das posições pelo resultado financeiro.


## 10. Persistência dos dados

O sistema utiliza arquivos CSV para manter os dados entre execuções.

Os arquivos são armazenados na pasta `dados`:

- `dados/ativos.csv`: informações dos ativos cadastrados.
- `dados/operacoes.csv`: histórico de compras e vendas.

Ao iniciar a aplicação, os arquivos existentes são carregados. Durante o uso, as alterações nos ativos e as operações são gravadas nos arquivos correspondentes. Ao encerrar, o sistema também salva os dados.

A carteira inicia com saldo de R$ 10.000,00. As operações registradas atualizam o saldo e as posições de acordo com as regras implementadas.

## 11. Testes automatizados

O projeto utiliza JUnit 5 para verificar o funcionamento de partes importantes do sistema.

Os testes existentes contemplam:
- Registro de cotações no histórico de um ativo.
- Atualização do preço atual após o registro de uma cotação.
- Padronização do ticker para letras maiúsculas.
- Compra de ações e atualização do saldo e da posição.
- Bloqueio de compras com saldo insuficiente.
- Venda de ações e atualização da quantidade e do saldo.
- Bloqueio de vendas de quantidades superiores à posição disponível.

Para executar os testes, utilize:

```bash
mvn clean test
```

O resultado esperado é a conclusão do processo Maven com os testes aprovados.

## 12. Instalação e execução

### Pré-requisitos

- JDK 17 ou superior.
- Apache Maven instalado e configurado no PATH.
- Terminal ou prompt de comando.

### Passo 1 — Obter o projeto

Se o projeto estiver hospedado no GitHub, clone o repositório:

```bash
git clone URL
```

Entre na pasta do projeto:

```bash
cd InvestTrack
```

Se o projeto já estiver no computador, abra o terminal diretamente na pasta que contém o arquivo `pom.xml`.

### Passo 2 — Executar os testes

```bash
mvn clean test
```

### Passo 3 — Executar a aplicação

```bash
mvn exec:java -Dexec.mainClass="br.com.investtrack.App"
```

### Passo 4 — Utilizar o sistema

Após iniciar a aplicação, o menu será exibido no terminal. Digite o número da funcionalidade desejada e siga as instruções apresentadas.

Os arquivos CSV serão utilizados para armazenar os dados na pasta `dados` do diretório de execução.

## 13. Exemplo de utilização

Considere uma carteira com saldo inicial de R$ 10.000,00.

O usuário cadastra o ativo PETR4 com preço de R$ 32,50 e realiza uma compra de 10 ações, com taxa de R$ 2,00.

O valor total da compra será:

**10 × R$ 32,50 + R$ 2,00 = R$ 327,00**

O saldo após a compra será:

**R$ 10.000,00 − R$ 327,00 = R$ 9.673,00**

A carteira passa a apresentar uma posição de 10 ações de PETR4, com custo médio de R$ 32,50 por ação, desconsiderando a taxa no cálculo desse custo médio.

A operação fica registrada no histórico e pode ser recuperada na próxima execução do sistema por meio do arquivo CSV.

## 14. Uso de inteligência artificial


| Uso / Aplicação : 

| Apoio à programação | Auxílio na compreensão e organização de classes
| Apoio à depuração | Orientação na análise de erros e na interpretação de mensagens do compilador e dos testes. |
| Documentação 


## 15. Limitações conhecidas

- O sistema funciona no terminal, sem interface gráfica.
- As cotações são informadas manualmente, sem integração com APIs de mercado.
- A persistência utiliza arquivos CSV locais.
- O sistema possui finalidade acadêmica e não executa transações financeiras reais.
- A rentabilidade apresentada depende dos preços registrados pelo usuário.

## 16. Considerações finais

O InvestTrack aplica conceitos de programação orientada a objetos, coleções, validação de dados, persistência em arquivos, testes automatizados e organização de projetos Java com Maven.
