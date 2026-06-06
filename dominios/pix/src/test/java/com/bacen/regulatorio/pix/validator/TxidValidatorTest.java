package com.bacen.regulatorio.pix.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TxidValidatorTest {

    @Test @DisplayName("Res.BCB1 — txid com 26 caracteres deve ser aceito")
    void deveAceitarTxid26Caracteres() {
        assertThat(TxidValidator.validar("A2345678901234567890123456")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — txid com 35 caracteres deve ser aceito")
    void deveAceitarTxid35Caracteres() {
        assertThat(TxidValidator.validar("A2345678901234567890123456789012345")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — txid alfanumérico deve ser aceito")
    void deveAceitarTxidAlfanumerico() {
        assertThat(TxidValidator.validar("ABC123def456GHI789jkl012MNO")).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 — txid muito curto deve ser rejeitado")
    void deveRejeitarTxidCurto() {
        assertThat(TxidValidator.validar("abc123")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — txid muito longo deve ser rejeitado")
    void deveRejeitarTxidLongo() {
        assertThat(TxidValidator.validar("A2345678901234567890123456789012345678")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — txid com caracteres especiais deve ser rejeitado")
    void deveRejeitarTxidComEspeciais() {
        assertThat(TxidValidator.validar("A2345678901234567890123456-")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — txid nulo deve ser rejeitado")
    void deveRejeitarNulo() {
        assertThat(TxidValidator.validar(null)).isPresent();
    }

    @Test @DisplayName("Res.BCB1 — txid vazio deve ser rejeitado")
    void deveRejeitarVazio() {
        assertThat(TxidValidator.validar("")).isPresent();
    }
}
