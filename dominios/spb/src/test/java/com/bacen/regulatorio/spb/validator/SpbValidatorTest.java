package com.bacen.regulatorio.spb.validator;

import com.bacen.regulatorio.spb.enums.SistemaLiquidacao;
import com.bacen.regulatorio.spb.enums.TipoMensagemSPB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SpbValidatorTest {

    @Test @DisplayName("Lei 10214 — STR em horario comercial deve ser aceito")
    void deveAceitarStrEmHorarioComercial() {
        assertThat(SpbValidator.validarHorarioCorte(
                SistemaLiquidacao.STR, LocalTime.of(14, 0), DayOfWeek.MONDAY)).isEmpty();
    }

    @Test @DisplayName("Lei 10214 — STR apos as 17h deve ser rejeitado")
    void deveRejeitarStrAposCorte() {
        assertThat(SpbValidator.validarHorarioCorte(
                SistemaLiquidacao.STR, LocalTime.of(17, 30), DayOfWeek.MONDAY)).isPresent();
    }

    @Test @DisplayName("Lei 10214 — operacao no sabado deve ser rejeitada")
    void deveRejeitarOperacaoNoSabado() {
        assertThat(SpbValidator.validarHorarioCorte(
                SistemaLiquidacao.LBTR, LocalTime.of(10, 0), DayOfWeek.SATURDAY)).isPresent();
    }

    @Test @DisplayName("Lei 10214 — LBTR aceita PACS_008")
    void deveAceitarPacs008NoLbtr() {
        assertThat(SpbValidator.validarCompatibilidadeMensagem(
                TipoMensagemSPB.PACS_008, SistemaLiquidacao.LBTR)).isEmpty();
    }

    @Test @DisplayName("Lei 10214 — LBTR rejeita PAIN_001")
    void deveRejeitarPain001NoLbtr() {
        assertThat(SpbValidator.validarCompatibilidadeMensagem(
                TipoMensagemSPB.PAIN_001, SistemaLiquidacao.LBTR)).isPresent();
    }

    @Test @DisplayName("Lei 10214 — valor abaixo do minimo do LBTR deve ser rejeitado")
    void deveRejeitarValorAbaixoMinimoLbtr() {
        assertThat(SpbValidator.validarValorMinimo(
                new BigDecimal("50000.00"), SistemaLiquidacao.LBTR)).isPresent();
    }

    @Test @DisplayName("Lei 10214 — valor acima do minimo do LBTR deve ser aceito")
    void deveAceitarValorAcimaMinimoLbtr() {
        assertThat(SpbValidator.validarValorMinimo(
                new BigDecimal("150000.00"), SistemaLiquidacao.LBTR)).isEmpty();
    }
}
