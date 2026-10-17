
package br.com.investtrack;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AtivoTest {

    @Test
    void deveRegistrarCotacoesNoHistorico() {
        Ativo ativo = new Ativo(
            "petr4", "Petrobras", "Petróleo", 32.50
        );

        Cotacao primeira = new Cotacao(
            32.50, LocalDate.of(2026, 10, 15)
        );

        Cotacao segunda = new Cotacao(
            33.10, LocalDate.of(2026, 10, 16)
        );

        ativo.registrarCotacao(primeira);
        ativo.registrarCotacao(segunda);

        assertEquals(2, ativo.getHistorico().size());
        assertEquals(33.10, ativo.getPrecoAtual(), 0.001);
        assertEquals("PETR4", ativo.getTicker());
    }
}