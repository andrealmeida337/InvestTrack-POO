
package br.com.investtrack;

public class Posicao {
    private Ativo ativo;
    private int quantidade;
    private double custoMedio;

    public Posicao(Ativo ativo) {
        if (ativo == null) {
            throw new IllegalArgumentException(
                "O ativo não pode ser nulo."
            );
        }

        this.ativo = ativo;
        this.quantidade = 0;
        this.custoMedio = 0.0;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getCustoMedio() {
        return custoMedio;
    }

    public double getTotalInvestido() {
        return quantidade * custoMedio;
    }

    public double getValorAtual() {
        return quantidade * ativo.getPrecoAtual();
    }

    public double getLucroPrejuizo() {
        return getValorAtual() - getTotalInvestido();
    }

    public double getRentabilidadePercentual() {
        if (getTotalInvestido() == 0) {
            return 0.0;
        }

        return (getLucroPrejuizo() / getTotalInvestido()) * 100;
    }

    void registrarCompra(int quantidadeComprada, double preco) {
        if (quantidadeComprada <= 0) {
            throw new IllegalArgumentException(
                "A quantidade comprada deve ser positiva."
            );
        }

        if (!Double.isFinite(preco) || preco <= 0) {
            throw new IllegalArgumentException(
                "O preço da compra deve ser positivo."
            );
        }

        double valorAntigo = getTotalInvestido();
        double valorCompra = quantidadeComprada * preco;
        int novaQuantidade = quantidade + quantidadeComprada;

        custoMedio = (valorAntigo + valorCompra) / novaQuantidade;
        quantidade = novaQuantidade;
    }

    void registrarVenda(int quantidadeVendida) {
        if (quantidadeVendida <= 0) {
            throw new IllegalArgumentException(
                "A quantidade vendida deve ser positiva."
            );
        }

        if (quantidadeVendida > quantidade) {
            throw new IllegalArgumentException(
                "Você não possui essa quantidade para vender."
            );
        }

        quantidade -= quantidadeVendida;

        if (quantidade == 0) {
            custoMedio = 0.0;
        }
    }

    @Override
    public String toString() {
        return ativo.getTicker()
            + " | Quantidade: " + quantidade
            + " | Custo médio: R$ "
            + String.format("%.2f", custoMedio)
            + " | Valor atual: R$ "
            + String.format("%.2f", getValorAtual())
            + " | Resultado: R$ "
            + String.format("%.2f", getLucroPrejuizo());
    }
}
