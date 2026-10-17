
package br.com.investtrack;

public class Investidor {
    private String nome;
    private String email;

    public Investidor(String nome, String email) {
        setNome(nome);
        setEmail(email);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")
                || email.startsWith("@")
                || email.endsWith("@")) {
            throw new IllegalArgumentException(
                "O e-mail informado é inválido."
            );
        }

        this.email = email.trim();
    }

    @Override
    public String toString() {
        return "Investidor: " + nome
            + " | E-mail: " + email;
    }
}