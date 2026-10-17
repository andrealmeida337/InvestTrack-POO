
package br.com.investtrack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final Path PASTA = Path.of("dados");
    private static final Path ARQUIVO_ATIVOS = PASTA.resolve("ativos.csv");
    private static final Path ARQUIVO_OPERACOES = PASTA.resolve("operacoes.csv");
    private static final double SALDO_INICIAL = 10000.00;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Ativo> ativos = carregarAtivos();

        Investidor investidor = new Investidor(
                "Investidor Demo", "demo@investtrack.com");

        Carteira carteira = new Carteira(investidor, SALDO_INICIAL);
        carregarOperacoes(ativos, carteira);

        int opcao;

        do {
            System.out.println("\n==================================");
            System.out.println("          INVESTTRACK");
            System.out.println("==================================");
            System.out.println("Investidor: " + investidor.getNome());
            System.out.printf("Saldo disponível: R$ %.2f%n", carteira.getSaldo());
            System.out.println("----------------------------------");
            System.out.println("1.  Cadastrar ativo");
            System.out.println("2.  Listar ativos");
            System.out.println("3.  Buscar ativo");
            System.out.println("4.  Editar ativo");
            System.out.println("5.  Desativar ativo");
            System.out.println("6.  Comprar ações");
            System.out.println("7.  Vender ações");
            System.out.println("8.  Consultar carteira");
            System.out.println("9.  Consultar operações");
            System.out.println("10. Relatórios");
            System.out.println("11. Exportar dados CSV");
            System.out.println("0.  Salvar e sair");
            System.out.println("==================================");

            opcao = lerInt(scanner, "Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        cadastrarAtivo(scanner, ativos);
                        salvarAtivos(ativos);
                        break;
                    case 2:
                        listarAtivos(ativos);
                        break;
                    case 3:
                        buscarAtivoMenu(scanner, ativos);
                        break;
                    case 4:
                        editarAtivo(scanner, ativos);
                        salvarAtivos(ativos);
                        break;
                    case 5:
                        desativarAtivo(scanner, ativos);
                        salvarAtivos(ativos);
                        break;
                    case 6:
                        comprar(scanner, ativos, carteira);
                        salvarAtivos(ativos);
                        exportarOperacoes(carteira);
                        break;
                    case 7:
                        vender(scanner, ativos, carteira);
                        salvarAtivos(ativos);
                        exportarOperacoes(carteira);
                        break;
                    case 8:
                        consultarCarteira(carteira);
                        break;
                    case 9:
                        listarOperacoes(carteira);
                        break;
                    case 10:
                        gerarRelatorios(carteira);
                        break;
                    case 11:
                        salvarAtivos(ativos);
                        exportarOperacoes(carteira);
                        System.out.println("Dados exportados para a pasta dados.");
                        break;
                    case 0:
                        salvarAtivos(ativos);
                        exportarOperacoes(carteira);
                        System.out.println("Dados salvos. Até a próxima!");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Não foi possível concluir: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Erro ao salvar os arquivos: " + e.getMessage());
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static String lerTexto(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInt(Scanner scanner, String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(scanner, mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(Scanner scanner, String mensagem) {
        while (true) {
            try {
                String texto = lerTexto(scanner, mensagem).replace(",", ".");
                double valor = Double.parseDouble(texto);

                if (!Double.isFinite(valor)) {
                    throw new NumberFormatException();
                }

                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Número inválido. Exemplo: 32,50");
            }
        }
    }

    private static Ativo encontrarAtivo(List<Ativo> ativos, String ticker) {
        for (Ativo ativo : ativos) {
            if (ativo.getTicker().equalsIgnoreCase(ticker.trim())) {
                return ativo;
            }
        }
        return null;
    }

    private static Ativo pedirAtivo(Scanner scanner, List<Ativo> ativos) {
        Ativo ativo = encontrarAtivo(
                ativos, lerTexto(scanner, "Ticker do ativo: "));

        if (ativo == null) {
            System.out.println("Ativo não encontrado.");
        }

        return ativo;
    }

    private static void cadastrarAtivo(
            Scanner scanner, ArrayList<Ativo> ativos) {
        System.out.println("\n--- CADASTRAR ATIVO ---");
        String ticker = lerTexto(scanner, "Ticker: ").toUpperCase();

        if (encontrarAtivo(ativos, ticker) != null) {
            System.out.println("Já existe um ativo com esse ticker.");
            return;
        }

        String nome = lerTexto(scanner, "Nome da empresa: ");
        String setor = lerTexto(scanner, "Setor: ");
        double preco = lerDouble(scanner, "Preço atual: R$ ");

        Ativo ativo = new Ativo(ticker, nome, setor, preco);
        ativos.add(ativo);

        System.out.println("Ativo cadastrado com sucesso!");
        System.out.println(ativo);
    }

    private static void listarAtivos(List<Ativo> ativos) {
        System.out.println("\n--- ATIVOS CADASTRADOS ---");

        if (ativos.isEmpty()) {
            System.out.println("Nenhum ativo cadastrado.");
            return;
        }

        ativos.stream()
                .sorted(Comparator.comparing(Ativo::getTicker))
                .forEach(System.out::println);
    }

    private static void buscarAtivoMenu(
            Scanner scanner, List<Ativo> ativos) {
        Ativo ativo = pedirAtivo(scanner, ativos);

        if (ativo != null) {
            System.out.println(ativo);
            System.out.println("Cotações registradas: "
                    + ativo.getHistorico().size());

            for (Cotacao cotacao : ativo.getHistorico()) {
                System.out.println(cotacao);
            }
        }
    }

    private static void editarAtivo(
            Scanner scanner, List<Ativo> ativos) {
        Ativo ativo = pedirAtivo(scanner, ativos);

        if (ativo == null) {
            return;
        }

        String nome = lerTexto(scanner,
                "Novo nome (Enter mantém " + ativo.getNome() + "): ");
        String setor = lerTexto(scanner,
                "Novo setor (Enter mantém " + ativo.getSetor() + "): ");
        String textoPreco = lerTexto(scanner,
                "Novo preço (Enter mantém o atual): ");

        if (!nome.isEmpty()) {
            ativo.setNome(nome);
        }

        if (!setor.isEmpty()) {
            ativo.setSetor(setor);
        }

        if (!textoPreco.isEmpty()) {
            double preco;

            try {
                preco = Double.parseDouble(textoPreco.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Preço inválido.");
                return;
            }

            ativo.atualizarPreco(preco);
        }

        System.out.println("Ativo atualizado: " + ativo);
    }

    private static void desativarAtivo(
            Scanner scanner, List<Ativo> ativos) {
        Ativo ativo = pedirAtivo(scanner, ativos);

        if (ativo == null) {
            return;
        }

        ativo.desativar();
        System.out.println("Ativo desativado.");
    }

    private static void comprar(
            Scanner scanner, List<Ativo> ativos, Carteira carteira) {
        Ativo ativo = pedirAtivo(scanner, ativos);

        if (ativo == null) {
            return;
        }

        int quantidade = lerInt(scanner, "Quantidade: ");
        double preco = lerDouble(scanner, "Preço por ação: R$ ");
        double taxa = lerDouble(scanner, "Taxa da operação: R$ ");

        carteira.comprar(ativo, quantidade, preco, taxa);
        System.out.println("Compra realizada com sucesso!");
        System.out.println(carteira);
    }

    private static void vender(
            Scanner scanner, List<Ativo> ativos, Carteira carteira) {
        Ativo ativo = pedirAtivo(scanner, ativos);

        if (ativo == null) {
            return;
        }

        int quantidade = lerInt(scanner, "Quantidade para vender: ");
        double preco = lerDouble(scanner, "Preço por ação: R$ ");
        double taxa = lerDouble(scanner, "Taxa da operação: R$ ");

        carteira.vender(ativo, quantidade, preco, taxa);
        System.out.println("Venda realizada com sucesso!");
        System.out.println(carteira);
    }

    private static void consultarCarteira(Carteira carteira) {
        System.out.println("\n--- MINHA CARTEIRA ---");
        System.out.println(carteira);

        if (carteira.getPosicoes().isEmpty()) {
            System.out.println("Você ainda não possui ações.");
            return;
        }

        for (Posicao posicao : carteira.getPosicoes()) {
            System.out.println(posicao);
            System.out.printf("Rentabilidade: %.2f%%%n",
                    posicao.getRentabilidadePercentual());
        }
    }

    private static void listarOperacoes(Carteira carteira) {
        System.out.println("\n--- HISTÓRICO DE OPERAÇÕES ---");

        if (carteira.getOperacoes().isEmpty()) {
            System.out.println("Nenhuma operação registrada.");
            return;
        }

        for (Operacao operacao : carteira.getOperacoes()) {
            System.out.println(operacao);
            System.out.printf("Valor total: R$ %.2f%n",
                    operacao.getValorTotal());
        }
    }

    private static void gerarRelatorios(Carteira carteira) {
        System.out.println("\n========== RELATÓRIOS ==========");
        System.out.printf("1. Saldo disponível: R$ %.2f%n", carteira.getSaldo());
        System.out.printf("2. Total investido: R$ %.2f%n", carteira.getTotalInvestido());
        System.out.printf("3. Patrimônio atual: R$ %.2f%n", carteira.getPatrimonio());
        System.out.println("4. Posições abertas: " + carteira.getPosicoes().size());
        System.out.println("5. Total de operações: " + carteira.getOperacoes().size());

        if (!carteira.getPosicoes().isEmpty()) {
            Posicao maior = carteira.getPosicoes().stream()
                    .max(Comparator.comparingDouble(Posicao::getLucroPrejuizo))
                    .orElseThrow();

            Posicao menor = carteira.getPosicoes().stream()
                    .min(Comparator.comparingDouble(Posicao::getLucroPrejuizo))
                    .orElseThrow();

            System.out.println("6. Melhor resultado: " + maior.getAtivo().getTicker()
                    + " (R$ " + String.format("%.2f", maior.getLucroPrejuizo()) + ")");
            System.out.println("7. Pior resultado: " + menor.getAtivo().getTicker()
                    + " (R$ " + String.format("%.2f", menor.getLucroPrejuizo()) + ")");

            double soma = 0;

            for (Posicao posicao : carteira.getPosicoes()) {
                soma += posicao.getRentabilidadePercentual();
            }

            System.out.printf("8. Rentabilidade média simples: %.2f%%%n",
                    soma / carteira.getPosicoes().size());

            System.out.println("9. Ranking de posições por resultado:");

            carteira.getPosicoes().stream()
                    .sorted(Comparator.comparingDouble(Posicao::getLucroPrejuizo)
                            .reversed())
                    .forEach(posicao -> System.out.printf(
                            "%s: R$ %.2f%n",
                            posicao.getAtivo().getTicker(),
                            posicao.getLucroPrejuizo()));
        }

        System.out.println("================================");
    }

    private static ArrayList<Ativo> carregarAtivos() {
        ArrayList<Ativo> ativos = new ArrayList<>();

        if (!Files.exists(ARQUIVO_ATIVOS)) {
            return ativos;
        }

        try {
            List<String> linhas = Files.readAllLines(
                    ARQUIVO_ATIVOS, StandardCharsets.UTF_8);

            for (int i = 1; i < linhas.size(); i++) {
                String[] partes = linhas.get(i).split(",", -1);

                if (partes.length < 4) {
                    continue;
                }

                try {
                    Ativo ativo = new Ativo(
                            partes[0], partes[1], partes[2],
                            Double.parseDouble(partes[3]));

                    if (partes.length >= 5
                            && partes[4].equalsIgnoreCase("false")) {
                        ativo.desativar();
                    }

                    if (encontrarAtivo(ativos, ativo.getTicker()) == null) {
                        ativos.add(ativo);
                    }
                } catch (RuntimeException e) {
                    System.out.println("Linha de ativo inválida: " + (i + 1));
                }
            }

            System.out.println("Ativos carregados: " + ativos.size());

        } catch (IOException e) {
            System.out.println("Erro ao carregar ativos: " + e.getMessage());
        }

        return ativos;
    }

    private static void salvarAtivos(List<Ativo> ativos) throws IOException {
        Files.createDirectories(PASTA);

        List<String> linhas = new ArrayList<>();
        linhas.add("ticker,nome,setor,precoAtual,ativo");

        for (Ativo ativo : ativos) {
            linhas.add(
                    ativo.getTicker() + ","
                    + ativo.getNome().replace(",", " ") + ","
                    + ativo.getSetor().replace(",", " ") + ","
                    + ativo.getPrecoAtual() + ","
                    + ativo.isAtivo());
        }

        Files.write(ARQUIVO_ATIVOS, linhas, StandardCharsets.UTF_8);
    }

    private static void carregarOperacoes(
            List<Ativo> ativos, Carteira carteira) {
        if (!Files.exists(ARQUIVO_OPERACOES)) {
            return;
        }

        try {
            List<String> linhas = Files.readAllLines(
                    ARQUIVO_OPERACOES, StandardCharsets.UTF_8);

            int carregadas = 0;

            for (int i = 1; i < linhas.size(); i++) {
                String[] partes = linhas.get(i).split(",", -1);

                if (partes.length != 8) {
                    System.out.println("Linha de operação inválida: " + (i + 1));
                    continue;
                }

                try {
                    LocalDate data = LocalDate.parse(partes[0]);
                    Operacao.Tipo tipo = Operacao.Tipo.valueOf(partes[1]);
                    Ativo ativo = encontrarAtivo(ativos, partes[2]);

                    if (ativo == null) {
                        System.out.println("Ativo não encontrado: " + partes[2]);
                        continue;
                    }

                    int quantidade = Integer.parseInt(partes[3]);
                    double preco = Double.parseDouble(partes[4]);
                    double taxa = Double.parseDouble(partes[5]);

                    Operacao operacao = new Operacao(
                            tipo, ativo, quantidade, preco, taxa, data);

                    carteira.restaurarOperacao(operacao);
                    carregadas++;

                } catch (RuntimeException e) {
                    System.out.println("Operação inválida na linha "
                            + (i + 1) + ": " + e.getMessage());
                }
            }

            System.out.println("Operações restauradas: " + carregadas);

        } catch (IOException e) {
            System.out.println("Erro ao carregar operações: " + e.getMessage());
        }
    }

    private static void exportarOperacoes(Carteira carteira)
            throws IOException {
        Files.createDirectories(PASTA);

        List<String> linhas = new ArrayList<>();
        linhas.add("data,tipo,ticker,quantidade,precoUnitario,taxa,valorBruto,valorTotal");

        for (Operacao operacao : carteira.getOperacoes()) {
            linhas.add(
                    operacao.getData() + ","
                    + operacao.getTipo() + ","
                    + operacao.getAtivo().getTicker() + ","
                    + operacao.getQuantidade() + ","
                    + operacao.getPrecoUnitario() + ","
                    + operacao.getTaxa() + ","
                    + operacao.getValorBruto() + ","
                    + operacao.getValorTotal());
        }

        Files.write(ARQUIVO_OPERACOES, linhas, StandardCharsets.UTF_8);
    }
}