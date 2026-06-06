package com.bacen.regulatorio.pix.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QrCodePayloadValidatorTest {

    /**
     * Payload EMVCo válido para PIX com chave EVP (UUID sem hífen).
     * Estrutura mínima: PFI=01, Merchant Account com GUI PIX, Merchant Name, Merchant City, CRC.
     *
     * Tag 26 (Merchant Account Information):
     *   sub-tag 00 (GUI)  = "0014BR.GOV.BCB.PIX"      (18 chars)
     *   sub-tag 01 (chave) = "0132<32-char-UUID>"       (36 chars)
     *   valor total = 54 chars
     */
    private static final String CHAVE_EVP = "123e4567e89b42d3a456426614174000";
    private static final String MERCHANT_ACCOUNT_INFO =
            "0014BR.GOV.BCB.PIX" +
            "0132" + CHAVE_EVP;

    private static final String PAYLOAD_VALIDO =
            "000201" +          // PFI tag=00 len=02 val=01
            "2654" +            // Merchant Account Info tag=26 len=54
                MERCHANT_ACCOUNT_INFO +
            "52040000" +        // MCC tag=52 len=04 val=0000
            "5902LO" +          // Merchant Name tag=59 len=02
            "6002SA" +          // Merchant City tag=60 len=02
            "6304ABCD";         // CRC tag=63 len=04

    private static final String PAYLOAD_SEM_PIX =
            "000201" +          // PFI
            "52040000" +        // MCC
            "5902LO" +          // Merchant Name
            "6002SA" +          // Merchant City
            "6304ABCD";         // CRC

    @Test @DisplayName("Res.BCB1 Anexo II — payload com GUI PIX válido deve ser aceito")
    void deveAceitarPayloadValido() {
        assertThat(QrCodePayloadValidator.validarPayload(PAYLOAD_VALIDO)).isEmpty();
    }

    @Test @DisplayName("Res.BCB1 Anexo II — payload sem GUI PIX deve ser rejeitado")
    void deveRejeitarPayloadSemPix() {
        assertThat(QrCodePayloadValidator.validarPayload(PAYLOAD_SEM_PIX)).isPresent();
    }

    @Test @DisplayName("Res.BCB1 Anexo II — payload nulo deve ser rejeitado")
    void deveRejeitarNulo() {
        assertThat(QrCodePayloadValidator.validarPayload(null)).isPresent();
    }

    @Test @DisplayName("Res.BCB1 Anexo II — payload vazio deve ser rejeitado")
    void deveRejeitarVazio() {
        assertThat(QrCodePayloadValidator.validarPayload("")).isPresent();
    }

    @Test @DisplayName("Res.BCB1 Anexo II — payload sem CRC deve ser rejeitado")
    void deveRejeitarPayloadSemCrc() {
        String semCrc = "00020126360014BR.GOV.BCB.PIX0112123456520400005902LO6002SA";
        assertThat(QrCodePayloadValidator.validarPayload(semCrc)).isPresent();
    }

    @Test @DisplayName("Res.BCB1 Anexo II — extrairSecaoPix deve retornar seção PIX")
    void deveExtrairSecaoPix() {
        var secao = QrCodePayloadValidator.extrairSecaoPix(PAYLOAD_VALIDO);
        assertThat(secao).isPresent();
        assertThat(secao.get()).contains("BR.GOV.BCB.PIX");
        assertThat(secao.get()).contains(CHAVE_EVP);
    }

    @Test @DisplayName("Res.BCB1 Anexo II — extrairSecaoPix retorna vazio se não há PIX")
    void extrairSecaoPixDeveRetornarVazioSeSemPix() {
        assertThat(QrCodePayloadValidator.extrairSecaoPix(PAYLOAD_SEM_PIX)).isEmpty();
    }
}
