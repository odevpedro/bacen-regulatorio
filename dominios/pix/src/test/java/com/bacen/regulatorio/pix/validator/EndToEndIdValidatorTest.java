package com.bacen.regulatorio.pix.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EndToEndIdValidatorTest {

    @Test @DisplayName("Res.BCB1 — E2E ID válido deve ser aceito")
    void deveAceitarE2EIdValido() {
        assertThat(EndToEndIdValidator.validar("E1234567820240101123456ABC")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID com sequencial mínimo (1 char) deve ser aceito")
    void deveAceitarE2EIdSequencialMinimo() {
        assertThat(EndToEndIdValidator.validar("E1234567820240101123456A")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID com sequencial máximo (12 chars) deve ser aceito")
    void deveAceitarE2EIdSequencialMaximo() {
        assertThat(EndToEndIdValidator.validar("E1234567820240101123456ABCDEFGHIJKL")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID sem prefixo E deve ser rejeitado")
    void deveRejeitarSemPrefixoE() {
        assertThat(EndToEndIdValidator.validar("A1234567820240101123456ABC")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID muito curto deve ser rejeitado")
    void deveRejeitarMuitoCurto() {
        assertThat(EndToEndIdValidator.validar("E12345678202401011234")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID muito longo deve ser rejeitado")
    void deveRejeitarMuitoLongo() {
        assertThat(EndToEndIdValidator.validar("E1234567820240101123456" + "A".repeat(20))).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID com caracteres especiais deve ser rejeitado")
    void deveRejeitarCaracteresEspeciais() {
        assertThat(EndToEndIdValidator.validar("E1234567820240101123456AB-C")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — E2E ID nulo deve ser rejeitado")
    void deveRejeitarNulo() {
        assertThat(EndToEndIdValidator.validar(null)).isPresent();
    }
}
