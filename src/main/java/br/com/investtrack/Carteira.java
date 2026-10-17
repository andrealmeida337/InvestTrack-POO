
package br.com.investtrack;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carteira {
    private Investidor investidor;
    private double saldo;
    private final ArrayList<Posicao> posicoes;
    private final ArrayList<Operacao> operacoes;

    public Carteira(Investidor investidor, double saldoInicial) {
        if (investidor == null) {
            throw new IllegalArgumentException("O investidor é obrigatório.");
        }

        if (!Double.isFinite(saldoInicial) || saldoInicial < 0) {
            throw new IllegalArgumentException(
                    "O saldo inicial não pode ser negativo ou inválido.");
        }

        this.investidor = investidor;
        this.saldo = saldoInicial;
        this.posicoes = new ArrayList<>();
        this.operacoes = new ArrayList<>();
    }

    public Investidor getInvestidor() {
        return investidor;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Posicao> getPosicoes() {
        return Collections.unmodifiableList(posicoes);
    }

    public List<Operacao> getOperacoes() {
        return Collections.unmodifiableList(operacoes);
    }

    public void comprar(Ativo ativo, int quantidade,
                        double precoUnitario, double taxa) {
        validarAtivo(ativo);

        Operacao operacao = new Operacao(
                Operacao.Tipo.COMPRA, ativo, quantidade,
                precoUnitario, taxa, LocalDate.now());

        double total = operacao.getValorTotal();

        if (saldo < total) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente para realizar a compra.");
        }

        Posicao posicao = buscarPosicao(ativo);

        if (posicao == null) {
            Posicao novaPosicao = new Posicao(ativo);
            novaPosicao.registrarCompra(quantidade, precoUnitario);
            posicoes.add(novaPosicao);
        } else {
            posicao.registrarCompra(quantidade, precoUnitario);
        }

        saldo -= total;
        operacoes.add(operacao);
    }

    public void vender(Ativo ativo, int quantidade,
                       double precoUnitario, double taxa) {
        validarAtivo(ativo);

        Posicao posicao = buscarPosicao(ativo);

        if (posicao == null || quantidade > posicao.getQuantidade()) {
            throw new IllegalArgumentException(
                    "Você não possui essa quantidade para vender.");
        }

        Operacao operacao = new Operacao(
                Operacao.Tipo.VENDA, ativo, quantidade,
                precoUnitario, taxa, LocalDate.now());

        if (operacao.getValorTotal() < 0) {
            throw new IllegalArgumentException(
                    "A taxa não pode superar o valor bruto da venda.");
        }

        posicao.registrarVenda(quantidade);
        saldo += operacao.getValorTotal();
        operacoes.add(operacao);

        if (posicao.getQuantidade() == 0) {
            posicoes.remove(posicao);
        }
    }

    public void restaurarOperacao(Operacao operacao) {
        if (operacao == null) {
            throw new IllegalArgumentException(
                    "A operação não pode ser nula.");
        }

        Ativo ativo = operacao.getAtivo();

        if (operacao.getTipo() == Operacao.Tipo.COMPRA) {
            double total = operacao.getValorTotal();

            if (saldo < total) {
                throw new IllegalArgumentException(
                        "Saldo insuficiente ao restaurar compra.");
            }

            Posicao posicao = buscarPosicao(ativo);

            if (posicao == null) {
                Posicao novaPosicao = new Posicao(ativo);
                novaPosicao.registrarCompra(
                        operacao.getQuantidade(),
                        operacao.getPrecoUnitario());
                posicoes.add(novaPosicao);
            } else {
                posicao.registrarCompra(
                        operacao.getQuantidade(),
                        operacao.getPrecoUnitario());
            }

            saldo -= total;

        } else {
            Posicao posicao = buscarPosicao(ativo);

            if (posicao == null
                    || operacao.getQuantidade() > posicao.getQuantidade()) {
                throw new IllegalArgumentException(
                        "Histórico de venda incompatível com a posição.");
            }

            if (operacao.getValorTotal() < 0) {
                throw new IllegalArgumentException(
                        "Valor líquido da venda inválido.");
            }

            posicao.registrarVenda(operacao.getQuantidade());
            saldo += operacao.getValorTotal();

            if (posicao.getQuantidade() == 0) {
                posicoes.remove(posicao);
            }
        }

        // Guarda a operação original, inclusive a data.
        operacoes.add(operacao);
    }

    private Posicao buscarPosicao(Ativo ativo) {
        for (Posicao posicao : posicoes) {
            if (posicao.getAtivo().getTicker()
                    .equalsIgnoreCase(ativo.getTicker())) {
                return posicao;
            }
        }

        return null;
    }

    private void validarAtivo(Ativo ativo) {
        if (ativo == null) {
            throw new IllegalArgumentException("O ativo é obrigatório.");
        }

        if (!ativo.isAtivo()) {
            throw new IllegalArgumentException(
                    "Não é possível negociar um ativo desativado.");
        }
    }

    public double getTotalInvestido() {
        double total = 0;

        for (Posicao posicao : posicoes) {
            total += posicao.getTotalInvestido();
        }

        return total;
    }

    public double getPatrimonio() {
        double patrimonio = saldo;

        for (Posicao posicao : posicoes) {
            patrimonio += posicao.getValorAtual();
        }

        return patrimonio;
    }

    @Override
    public String toString() {
        return "Carteira de " + investidor.getNome()
                + " | Saldo: R$ " + String.format("%.2f", saldo)
                + " | Investido: R$ " + String.format("%.2f", getTotalInvestido())
                + " | Patrimônio: R$ " + String.format("%.2f", getPatrimonio());
    }
}