
package br.com.investtrack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ativo {
    private String ticker;
    private String nome;
    private String setor;
    private double precoAtual;
    private boolean ativo;
    private final ArrayList<Cotacao> historico;

    public Ativo(String ticker, String nome, String setor,
                 double precoAtual) {
        setTicker(ticker);
        setNome(nome);
        setSetor(setor);
        setPrecoAtual(precoAtual);
        this.ativo = true;
        this.historico = new ArrayList<>();
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        if (ticker == null || ticker.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O ticker não pode ficar vazio."
            );
        }
        this.ticker = ticker.trim().toUpperCase();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O nome não pode ficar vazio."
            );
        }
        this.nome = nome.trim();
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        if (setor == null || setor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O setor não pode ficar vazio."
            );
        }
        this.setor = setor.trim();
    }

    public double getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(double precoAtual) {
        if (!Double.isFinite(precoAtual) || precoAtual < 0) {
            throw new IllegalArgumentException(
                "O preço deve ser válido e não negativo."
            );
        }
        this.precoAtual = precoAtual;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizarPreco(double novoPreco) {
        setPrecoAtual(novoPreco);
    }

    public void registrarCotacao(Cotacao cotacao) {
        if (cotacao == null) {
            throw new IllegalArgumentException(
                "A cotação não pode ser nula."
            );
        }

        historico.add(cotacao);
        atualizarPreco(cotacao.getPreco());
    }

    public List<Cotacao> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    @Override
    public String toString() {
        return ticker + " - " + nome
            + " | Setor: " + setor
            + " | Preço: R$ "
            + String.format("%.2f", precoAtual)
            + " | Ativo: " + (ativo ? "Sim" : "Não");
    }
}