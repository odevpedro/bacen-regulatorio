package com.bacen.regulatorio.pix.validator;

import com.bacen.regulatorio.pix.enums.TipoChavePix;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChavePixValidatorTest {

    @Test @DisplayName("Res.BCB1 — CPF válido deve ser aceito como chave")
    void deveAceitarCpfValido() {
        assertThat(ChavePixValidator.isValid("52998224725", TipoChavePix.CPF)).isTrue();
    }

    @Test @DisplayName("Res.BCB1 — CNPJ válido deve ser aceito como chave")
    void deveAceitarCnpjValido() {
        assertThat(ChavePixValidator.isValid("11222333000181", TipoChavePix.CNPJ)).isTrue();
    }

    @Test @DisplayName("Res.BCB1 — e-mail válido deve ser aceito")
    void deveAceitarEmailValido() {
        assertThat(ChavePixValidator.isValid("usuario@banco.com.br", TipoChavePix.EMAIL)).isTrue();
    }

    @Test @DisplayName("Res.BCB1 — e-mail acima de 77 caracteres deve ser rejeitado")
    void deveRejeitarEmailLongo() {
        String emailLongo = "a".repeat(70) + "@banco.com";
        assertThat(ChavePixValidator.isValid(emailLongo, TipoChavePix.EMAIL)).isFalse();
    }

    @Test @DisplayName("Res.BCB1 — telefone no formato correto deve ser aceito")
    void deveAceitarTelefoneValido() {
        assertThat(ChavePixValidator.isValid("+5511987654321", TipoChavePix.TELEFONE)).isTrue();
        assertThat(ChavePixValidator.isValid("+5511912345678", TipoChavePix.TELEFONE)).isTrue();
    }

    @Test @DisplayName("Res.BCB1 — telefone sem DDI deve ser rejeitado")
    void deveRejeitarTelefoneSemDDI() {
        assertThat(ChavePixValidator.isValid("11987654321", TipoChavePix.TELEFONE)).isFalse();
    }

    @Test @DisplayName("Res.BCB1 — EVP no formato UUID v4 deve ser aceito")
    void deveAceitarEvpValido() {
        assertThat(ChavePixValidator.isValid(
                "123e4567-e89b-42d3-a456-426614174000", TipoChavePix.EVP)).isTrue();
    }

    @Test @DisplayName("Res.BCB1 — detecção automática de tipo por formato")
    void deveDetectarTipoAutomaticamente() {
        assertThat(ChavePixValidator.detectarTipo("52998224725")).isEqualTo(TipoChavePix.CPF);
        assertThat(ChavePixValidator.detectarTipo("11222333000181")).isEqualTo(TipoChavePix.CNPJ);
        assertThat(ChavePixValidator.detectarTipo("user@email.com")).isEqualTo(TipoChavePix.EMAIL);
        assertThat(ChavePixValidator.detectarTipo("+5511987654321")).isEqualTo(TipoChavePix.TELEFONE);
    }
}
