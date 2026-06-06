package com.bacen.regulatorio.credito.validator;

import com.bacen.regulatorio.credito.enums.TipoCredito;
import com.bacen.regulatorio.credito.valueobject.CET;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CreditoValidatorTest {

    @Test @DisplayName("Res.4558 — CDC com prazo dentro do limite deve ser aceito")
    void deveAceitarPrazoCdcDentroDoLimite() {
        assertThat(CreditoValidator.validarPrazoMaximo(TipoCredito.FINANCIAMENTO_BENS, 48)).isEmpty();
    }

    @Test @DisplayName("Res.4558 — CDC com prazo acima do limite deve ser rejeitado")
    void deveRejeitarPrazoCdcAcimaDoLimite() {
        assertThat(CreditoValidator.validarPrazoMaximo(TipoCredito.FINANCIAMENTO_BENS, 72)).isPresent();
    }

    @Test @DisplayName("Res.4558 — credito imobiliario com prazo ate 420 meses deve ser aceito")
    void deveAceitarPrazoImobiliario() {
        assertThat(CreditoValidator.validarPrazoMaximo(TipoCredito.FINANCIAMENTO_IMOBILIARIO, 360)).isEmpty();
        assertThat(CreditoValidator.validarPrazoMaximo(TipoCredito.FINANCIAMENTO_IMOBILIARIO, 420)).isEmpty();
    }

    @Test @DisplayName("Res.4558 — credito imobiliario acima de 420 meses deve ser rejeitado")
    void deveRejeitarPrazoImobiliarioAcima() {
        assertThat(CreditoValidator.validarPrazoMaximo(TipoCredito.FINANCIAMENTO_IMOBILIARIO, 480)).isPresent();
    }

    @Test @DisplayName("Res.4558 — CET valido deve ser aceito")
    void deveAceitarCetValido() {
        var cet = new CET(new BigDecimal("25.00"), new BigDecimal("1500.00"));
        assertThat(CreditoValidator.validarCET(cet, new BigDecimal("30.00"))).isEmpty();
    }

    @Test @DisplayName("Res.4558 — CET acima do limite deve ser rejeitado")
    void deveRejeitarCetAcimaDoLimite() {
        var cet = new CET(new BigDecimal("35.00"), new BigDecimal("2000.00"));
        assertThat(CreditoValidator.validarCET(cet, new BigDecimal("30.00"))).isPresent();
    }

    @Test @DisplayName("Res.4558 — CET nulo deve ser rejeitado")
    void deveRejeitarCetNulo() {
        assertThat(CreditoValidator.validarCET(null, new BigDecimal("30.00"))).isPresent();
    }

    @Test @DisplayName("Res.4558 — CET.calcular deve produzir valor consistente")
    void deveCalcularCetCorretamente() {
        var cet = CET.calcular(new BigDecimal("10000.00"), new BigDecimal("12000.00"), 12);
        assertThat(cet.valorPercentualAnual()).isPositive();
        assertThat(cet.valorMonetario()).isEqualByComparingTo("2000");
    }
}
