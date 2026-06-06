package com.bacen.regulatorio.cambio.validator;

import com.bacen.regulatorio.cambio.enums.FinalidadeCambio;
import com.bacen.regulatorio.cambio.enums.Moeda;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CambioValidatorTest {

    @Test @DisplayName("Res.BCB277 — USD deve ser aceito como moeda de origem")
    void deveAceitarUsdElegivel() {
        assertThat(CambioValidator.validarMoedaElegivel(Moeda.USD)).isEmpty();
    }

    @Test @DisplayName("Res.BCB277 — BRL nao pode ser moeda de origem")
    void deveRejeitarBrlComoOrigem() {
        assertThat(CambioValidator.validarMoedaElegivel(Moeda.BRL)).isPresent();
    }

    @Test @DisplayName("Res.BCB277 — valor positivo deve ser aceito")
    void deveAceitarValorPositivo() {
        assertThat(CambioValidator.validarValorMinimo(new BigDecimal("1000.00"), Moeda.USD)).isEmpty();
    }

    @Test @DisplayName("Res.BCB277 — valor nulo deve ser rejeitado")
    void deveRejeitarValorNulo() {
        assertThat(CambioValidator.validarValorMinimo(null, Moeda.USD)).isPresent();
    }

    @Test @DisplayName("Circ.3691 — prazo de viagem internacional dentro do limite")
    void deveAceitarPrazoViagemDentroDoLimite() {
        assertThat(CambioValidator.validarPrazoLiquidacao(FinalidadeCambio.VIAGEM_INTERNACIONAL, 15)).isEmpty();
    }

    @Test @DisplayName("Circ.3691 — prazo de viagem internacional acima do limite")
    void deveRejeitarPrazoViagemAcimaDoLimite() {
        assertThat(CambioValidator.validarPrazoLiquidacao(FinalidadeCambio.VIAGEM_INTERNACIONAL, 45)).isPresent();
    }

    @Test @DisplayName("Circ.3691 — prazo de importacao dentro do limite")
    void deveAceitarPrazoImportacaoDentroDoLimite() {
        assertThat(CambioValidator.validarPrazoLiquidacao(FinalidadeCambio.PAGAMENTO_IMPORTACAO, 180)).isEmpty();
    }

    @Test @DisplayName("Res.BCB277 — variacao de taxa dentro do limite deve ser aceita")
    void deveAceitarVariacaoTaxaDentroDoLimite() {
        assertThat(CambioValidator.validarVariacaoTaxa(
                new BigDecimal("5.05"), new BigDecimal("5.00"), new BigDecimal("2.00"))).isEmpty();
    }

    @Test @DisplayName("Res.BCB277 — variacao de taxa acima do limite deve ser rejeitada")
    void deveRejeitarVariacaoTaxaAcimaDoLimite() {
        assertThat(CambioValidator.validarVariacaoTaxa(
                new BigDecimal("5.50"), new BigDecimal("5.00"), new BigDecimal("2.00"))).isPresent();
    }
}
