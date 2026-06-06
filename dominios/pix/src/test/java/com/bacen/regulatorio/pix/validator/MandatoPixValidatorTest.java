package com.bacen.regulatorio.pix.validator;

import com.bacen.regulatorio.pix.enums.PeriodicidadePixAutomatico;
import com.bacen.regulatorio.pix.enums.StatusMandatoPix;
import com.bacen.regulatorio.pix.valueobject.MandatoPix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MandatoPixValidatorTest {

    private final MandatoPix mandato = new MandatoPix(
            "MAND001", "52998224725", "11222333000181",
            new BigDecimal("500.00"), new BigDecimal("2000.00"), 10,
            PeriodicidadePixAutomatico.MENSAL,
            LocalDate.now().minusMonths(1), LocalDate.now().plusYears(1),
            StatusMandatoPix.ATIVO);

    @Test @DisplayName("Res.BCB191 — mandato ativo deve ser aceito")
    void deveAceitarMandatoAtivo() {
        assertThat(MandatoPixValidator.validarMandatoAtivo(mandato)).isEmpty();
    }

    @Test @DisplayName("Res.BCB191 — mandato expirado deve ser rejeitado")
    void deveRejeitarMandatoExpirado() {
        var expirado = new MandatoPix("MAND002", "52998224725", "11222333000181",
                new BigDecimal("500.00"), null, 10,
                PeriodicidadePixAutomatico.MENSAL,
                LocalDate.now().minusMonths(6), LocalDate.now().minusDays(1),
                StatusMandatoPix.ATIVO);
        assertThat(MandatoPixValidator.validarMandatoAtivo(expirado)).isPresent();
    }

    @Test @DisplayName("Res.BCB191 — valor dentro do limite deve ser aceito")
    void deveAceitarValorDentroDoLimite() {
        assertThat(MandatoPixValidator.validarValorCobranca(mandato, new BigDecimal("300.00"))).isEmpty();
    }

    @Test @DisplayName("Res.BCB191 — valor acima do limite deve ser rejeitado")
    void deveRejeitarValorAcimaDoLimite() {
        assertThat(MandatoPixValidator.validarValorCobranca(mandato, new BigDecimal("600.00"))).isPresent();
    }

    @Test @DisplayName("Res.BCB191 — valor acumulado no mes dentro do limite")
    void deveAceitarValorAcumuladoDentroDoLimite() {
        assertThat(MandatoPixValidator.validarValorAcumuladoMes(
                mandato, new BigDecimal("1500.00"), new BigDecimal("300.00"))).isEmpty();
    }

    @Test @DisplayName("Res.BCB191 — valor acumulado no mes acima do limite")
    void deveRejeitarValorAcumuladoAcimaDoLimite() {
        assertThat(MandatoPixValidator.validarValorAcumuladoMes(
                mandato, new BigDecimal("1800.00"), new BigDecimal("300.00"))).isPresent();
    }

    @Test @DisplayName("Res.BCB191 — quantidade dentro do limite")
    void deveAceitarQuantidadeDentroDoLimite() {
        assertThat(MandatoPixValidator.validarQuantidadeCobrancas(mandato, 5)).isEmpty();
    }

    @Test @DisplayName("Res.BCB191 — quantidade no limite deve ser rejeitada")
    void deveRejeitarQuantidadeNoLimite() {
        assertThat(MandatoPixValidator.validarQuantidadeCobrancas(mandato, 10)).isPresent();
    }

    @Test @DisplayName("Res.BCB191 — mandato.isAtivo() retorna true quando valido")
    void mandatoAtivoRetornaTrue() {
        assertThat(mandato.isAtivo()).isTrue();
    }

    @Test @DisplayName("Res.BCB191 — mandato cancelado retorna isAtivo false")
    void mandatoCanceladoRetornaFalse() {
        var cancelado = new MandatoPix("MAND003", "52998224725", "11222333000181",
                new BigDecimal("500.00"), null, 10,
                PeriodicidadePixAutomatico.MENSAL,
                LocalDate.now().minusMonths(1), LocalDate.now().plusYears(1),
                StatusMandatoPix.CANCELADO_PAGADOR);
        assertThat(cancelado.isAtivo()).isFalse();
    }
}
