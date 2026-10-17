
package br.com.investtrack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarteiraTest {

    @Test
    void compraDeveDescontarSaldoEAtualizarPosicao() {
        Investidor investidor =
            new Investidor("Ana", "ana@email.com");

        Carteira carteira = new Carteira(investidor, 1000.00);

        Ativo ativo =
            new Ativo("PETR4", "Petrobras", "Petróleo", 30.00);

        carteira.comprar(ativo, 10, 30.00, 2.00);

        assertEquals(698.00, carteira.getSaldo(), 0.001);
        assertEquals(1, carteira.getPosicoes().size());
        assertEquals(10,
            carteira.getPosicoes().get(0).getQuantidade());
        assertEquals(1, carteira.getOperacoes().size());
    }

    @Test
    void naoDeveComprarComSaldoInsuficiente() {
        Investidor investidor =
            new Investidor("Ana", "ana@email.com");

        Carteira carteira = new Carteira(investidor, 100.00);

        Ativo ativo =
            new Ativo("PETR4", "Petrobras", "Petróleo", 30.00);

        assertThrows(
            IllegalArgumentException.class,
            () -> carteira.comprar(ativo, 10, 30.00, 0)
        );

        assertEquals(100.00, carteira.getSaldo(), 0.001);
        assertTrue(carteira.getPosicoes().isEmpty());
        assertTrue(carteira.getOperacoes().isEmpty());
    }

    @Test
    void vendaDeveAtualizarSaldoEQuantidade() {
        Investidor investidor =
            new Investidor("Ana", "ana@email.com");

        Carteira carteira = new Carteira(investidor, 1000.00);

        Ativo ativo =
            new Ativo("PETR4", "Petrobras", "Petróleo", 30.00);

        carteira.comprar(ativo, 10, 30.00, 0);
        carteira.vender(ativo, 4, 35.00, 2.00);

        assertEquals(838.0, carteira.getSaldo(), 0.001);
        assertEquals(6,carteira.getPosicoes().get(0).getQuantidade());
        assertEquals(2, carteira.getOperacoes().size());
    }

    @Test
    void naoDeveVenderMaisAcoesDoQuePossui() {
        Investidor investidor =
            new Investidor("Ana", "ana@email.com");

        Carteira carteira = new Carteira(investidor, 1000.00);

        Ativo ativo =
            new Ativo("PETR4", "Petrobras", "Petróleo", 30.00);

        carteira.comprar(ativo, 5, 30.00, 0);

        assertThrows(
            IllegalArgumentException.class,
            () -> carteira.vender(ativo, 6, 35.00, 0)
        );

        assertEquals(850.00, carteira.getSaldo(), 0.001);
        assertEquals(5,
            carteira.getPosicoes().get(0).getQuantidade());
        assertEquals(1, carteira.getOperacoes().size());
    }
}