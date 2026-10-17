
package br.com.investtrack;

import java.time.LocalDate;

public class Cotacao {
    private double preco;
    private LocalDate data;

    public Cotacao(double preco, LocalDate data) {
        setPreco(preco);
        setData(data);
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (!Double.isFinite(preco) || preco < 0) {
            throw new IllegalArgumentException(
                "O preço deve ser válido e não negativo."
            );
        }

        this.preco = preco;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException(
                "A data não pode ser nula."
            );
        }

        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "A data da cotação não pode ser futura."
            );
        }

        this.data = data;
    }

    @Override
    public String toString() {
        return "Data: " + data
            + " | Preço: R$ "
            + String.format("%.2f", preco);
    }
}
