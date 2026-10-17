
package br.com.investtrack;

import java.time.LocalDate;

public class Operacao {

    public enum Tipo {
        COMPRA,
        VENDA
    }

    private Tipo tipo;
    private Ativo ativo;
    private int quantidade;
    private double precoUnitario;
    private double taxa;
    private LocalDate data;

    public Operacao(
            Tipo tipo,
            Ativo ativo,
            int quantidade,
            double precoUnitario,
            double taxa,
            LocalDate data) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                "O tipo da operação é obrigatório."
            );
        }

        if (ativo == null) {
            throw new IllegalArgumentException(
                "O ativo é obrigatório."
            );
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser positiva."
            );
        }

        if (!Double.isFinite(precoUnitario)
                || precoUnitario <= 0) {
            throw new IllegalArgumentException(
                "O preço unitário deve ser positivo."
            );
        }

        if (!Double.isFinite(taxa) || taxa < 0) {
            throw new IllegalArgumentException(
                "A taxa não pode ser negativa."
            );
        }

        if (data == null || data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "A data é inválida ou está no futuro."
            );
        }

        this.tipo = tipo;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.taxa = taxa;
        this.data = data;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getTaxa() {
        return taxa;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValorBruto() {
        return quantidade * precoUnitario;
    }

    public double getValorTotal() {
        if (tipo == Tipo.COMPRA) {
            return getValorBruto() + taxa;
        }

        return getValorBruto() - taxa;
    }

    @Override
    public String toString() {
        return data + " | " + tipo
            + " | " + ativo.getTicker()
            + " | Quantidade: " + quantidade
            + " | Preço: R$ "
            + String.format("%.2f", precoUnitario)
            + " | Taxa: R$ "
            + String.format("%.2f", taxa)
            + " | Valor bruto: R$ "
            + String.format("%.2f", getValorBruto());
    }
}