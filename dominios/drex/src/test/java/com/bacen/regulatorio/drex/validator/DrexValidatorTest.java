package com.bacen.regulatorio.drex.validator;

import com.bacen.regulatorio.drex.enums.StatusLiquidacaoDrex;
import com.bacen.regulatorio.drex.enums.TipoAtivoDigital;
import com.bacen.regulatorio.drex.valueobject.ReservaLiquidez;
import com.bacen.regulatorio.drex.valueobject.TransacaoDrex;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DrexValidatorTest {

    private final ReservaLiquidez reserva = new ReservaLiquidez(
            "52998224725", new BigDecimal("5000.00"), new BigDecimal("1000.00"));

    private final TransacaoDrex transacao = new TransacaoDrex(
            "TXN001", TipoAtivoDigital.DREX, new BigDecimal("3000.00"),
            "52998224725", "11222333000181",
            LocalDateTime.now(), StatusLiquidacaoDrex.PENDENTE_LIQUIDACAO);

    @Test @DisplayName("DREX — liquidez suficiente deve ser aceita")
    void deveAceitarLiquidezSuficiente() {
        assertThat(DrexValidator.validarLiquidez(reserva, transacao)).isEmpty();
    }

    @Test @DisplayName("DREX — liquidez insuficiente deve ser rejeitada")
    void deveRejeitarLiquidezInsuficiente() {
        var txGrande = new TransacaoDrex("TXN002", TipoAtivoDigital.DREX,
                new BigDecimal("10000.00"), "52998224725", "11222333000181",
                LocalDateTime.now(), StatusLiquidacaoDrex.PENDENTE_LIQUIDACAO);
        assertThat(DrexValidator.validarLiquidez(reserva, txGrande)).isPresent();
    }

    @Test @DisplayName("DREX — atomic settlement com liquidez deve passar")
    void deveAceitarAtomicSettlementComLiquidez() {
        assertThat(DrexValidator.validarAtomicSettlement(reserva, transacao)).isEmpty();
    }

    @Test @DisplayName("DREX — valor minimo deve ser aceito")
    void deveAceitarValorMinimo() {
        assertThat(DrexValidator.validarValorMinimo(new BigDecimal("0.01"))).isEmpty();
    }

    @Test @DisplayName("DREX — valor abaixo do minimo deve ser rejeitado")
    void deveRejeitarValorAbaixoMinimo() {
        assertThat(DrexValidator.validarValorMinimo(new BigDecimal("0.001"))).isPresent();
    }

    @Test @DisplayName("ReservaLiquidez — saldo total deve ser soma dos saldos")
    void saldoTotalDeveSerSoma() {
        assertThat(reserva.saldoTotal()).isEqualByComparingTo("6000.00");
    }

    @Test @DisplayName("ReservaLiquidez — possuiLiquidez verdadeiro quando saldo suficiente")
    void possuiLiquidezVerdadeiro() {
        assertThat(reserva.possuiLiquidez(new BigDecimal("5000.00"))).isTrue();
        assertThat(reserva.possuiLiquidez(new BigDecimal("5000.01"))).isFalse();
    }
}
