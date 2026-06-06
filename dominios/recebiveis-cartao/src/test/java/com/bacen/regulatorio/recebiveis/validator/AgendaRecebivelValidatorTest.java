package com.bacen.regulatorio.recebiveis.validator;

import com.bacen.regulatorio.recebiveis.valueobject.AgendaRecebivel;
import com.bacen.regulatorio.recebiveis.valueobject.UnidadeRecebivelId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AgendaRecebivelValidatorTest {

    private final LocalDate hoje = LocalDate.now();
    private final UnidadeRecebivelId unidadeId = new UnidadeRecebivelId(
            "VCC", "52998224725", "11222333000181", hoje.plusMonths(6));

    private final AgendaRecebivel agenda = new AgendaRecebivel(
            unidadeId,
            List.of(
                    new AgendaRecebivel.EntradaAgenda(hoje.plusMonths(1), new BigDecimal("1000.00")),
                    new AgendaRecebivel.EntradaAgenda(hoje.plusMonths(2), new BigDecimal("1500.00")),
                    new AgendaRecebivel.EntradaAgenda(hoje.plusMonths(3), new BigDecimal("2000.00"))
            ),
            new BigDecimal("500.00"));

    @Test @DisplayName("Res.4734 — saldo projetado cobre gravame dentro do prazo")
    void deveAceitarCoberturaSuficiente() {
        assertThat(AgendaRecebivelValidator.validarCoberturaGravame(
                agenda, new BigDecimal("2000.00"), hoje.plusMonths(2))).isEmpty();
    }

    @Test @DisplayName("Res.4734 — saldo projetado insuficiente deve ser rejeitado")
    void deveRejeitarCoberturaInsuficiente() {
        assertThat(AgendaRecebivelValidator.validarCoberturaGravame(
                agenda, new BigDecimal("5000.00"), hoje.plusMonths(2))).isPresent();
    }

    @Test @DisplayName("Res.4734 — ordem cronologica valida deve ser aceita")
    void deveAceitarOrdemCronologicaValida() {
        assertThat(AgendaRecebivelValidator.validarOrdemCronologica(agenda)).isEmpty();
    }

    @Test @DisplayName("Res.4734 — ordem cronologica invalida deve ser rejeitada")
    void deveRejeitarOrdemCronologicaInvalida() {
        var agendaInvalida = new AgendaRecebivel(unidadeId,
                List.of(
                        new AgendaRecebivel.EntradaAgenda(hoje.plusMonths(3), new BigDecimal("1000.00")),
                        new AgendaRecebivel.EntradaAgenda(hoje.plusMonths(1), new BigDecimal("500.00"))
                ), BigDecimal.ZERO);
        assertThat(AgendaRecebivelValidator.validarOrdemCronologica(agendaInvalida)).isPresent();
    }

    @Test @DisplayName("Res.4734 — data operacao antes da ultima liquidacao deve ser aceita")
    void deveAceitarDataOperacaoAntesUltimaLiquidacao() {
        assertThat(AgendaRecebivelValidator.validarPrazoMaximoLiquidacao(
                agenda, hoje.plusMonths(2))).isEmpty();
    }

    @Test @DisplayName("Res.4734 — data operacao depois da ultima liquidacao deve ser rejeitada")
    void deveRejeitarDataOperacaoDepoisUltimaLiquidacao() {
        assertThat(AgendaRecebivelValidator.validarPrazoMaximoLiquidacao(
                agenda, hoje.plusMonths(6))).isPresent();
    }

    @Test @DisplayName("Res.4734 — saldoProjetadoTotal deve considerar todas as entradas")
    void saldoProjetadoTotalCorreto() {
        assertThat(agenda.saldoProjetadoTotal()).isEqualByComparingTo("5000.00");
    }
}
