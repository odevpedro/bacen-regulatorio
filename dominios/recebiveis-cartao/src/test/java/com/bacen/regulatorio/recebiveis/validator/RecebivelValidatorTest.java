package com.bacen.regulatorio.recebiveis.validator;

import com.bacen.regulatorio.recebiveis.enums.MotivoRecusa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RecebivelValidatorTest {

    @Test @DisplayName("Res.4734 — prioridade duplicada deve ser recusada")
    void deveRecusarPrioridadeDuplicada() {
        Optional<MotivoRecusa> result = RecebivelValidator.validarPrioridade(true);
        assertThat(result).contains(MotivoRecusa.PRIORIDADE_DUPLICADA);
    }

    @Test @DisplayName("Res.4734 — prioridade disponível deve passar")
    void deveAceitarPrioridadeDisponivel() {
        assertThat(RecebivelValidator.validarPrioridade(false)).isEmpty();
    }

    @Test @DisplayName("Res.4734 — valor acima do saldo deve ser recusado")
    void deveRecusarValorAcimaDoSaldo() {
        Optional<MotivoRecusa> result = RecebivelValidator.validarSaldo(
                new BigDecimal("1500.00"), new BigDecimal("1000.00"));
        assertThat(result).contains(MotivoRecusa.SALDO_INSUFICIENTE);
    }

    @Test @DisplayName("Res.4734 — valor igual ao saldo deve ser aceito")
    void deveAceitarValorIgualAoSaldo() {
        assertThat(RecebivelValidator.validarSaldo(
                new BigDecimal("1000.00"), new BigDecimal("1000.00"))).isEmpty();
    }

    @Test @DisplayName("Circ.4016 — indicador SI deve ser aceito")
    void deveAceitarIndicadorSI() {
        assertThat(RecebivelValidator.validarInteroperabilidade("SI")).isEmpty();
        assertThat(RecebivelValidator.validarInteroperabilidade("CI")).isEmpty();
        assertThat(RecebivelValidator.validarInteroperabilidade("NI")).isEmpty();
    }

    @Test @DisplayName("Circ.4016 — indicador inválido deve ser recusado")
    void deveRecusarIndicadorInvalido() {
        assertThat(RecebivelValidator.validarInteroperabilidade("XX"))
                .contains(MotivoRecusa.INTEROPERABILIDADE_INVALIDA);
        assertThat(RecebivelValidator.validarInteroperabilidade(null))
                .contains(MotivoRecusa.INTEROPERABILIDADE_INVALIDA);
    }

    @Test @DisplayName("Circ.4016 — CPF válido deve passar")
    void deveAceitarCpfValido() {
        assertThat(RecebivelValidator.validarCpfCnpj("52998224725")).isEmpty();
    }

    @Test @DisplayName("Circ.4016 — CPF com todos dígitos iguais deve ser recusado")
    void deveRecusarCpfComDigitosIguais() {
        assertThat(RecebivelValidator.validarCpfCnpj("00000000000"))
                .contains(MotivoRecusa.CPF_CNPJ_INVALIDO);
    }

    @Test @DisplayName("Circ.4016 — CNPJ válido deve passar")
    void deveAceitarCnpjValido() {
        assertThat(RecebivelValidator.validarCpfCnpj("11222333000181")).isEmpty();
    }

    @Test @DisplayName("Res.4734 — alteração com novo valor dentro do saldo recalculado deve passar")
    void deveAceitarAlteracaoDentroDoSaldo() {
        // saldo=500, valorAtual=1000, novoValor=1200 → saldoRecuperado=1500, 1200<=1500 OK
        assertThat(RecebivelValidator.validarSaldoAlteracao(
                new BigDecimal("1200.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("500.00"))).isEmpty();
    }
}
