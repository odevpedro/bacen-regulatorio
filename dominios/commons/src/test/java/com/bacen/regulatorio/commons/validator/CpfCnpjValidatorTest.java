package com.bacen.regulatorio.commons.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfCnpjValidatorTest {

    @Test @DisplayName("CPF válido deve ser aceito")
    void deveAceitarCpfValido() {
        assertThat(CpfCnpjValidator.isValid("52998224725")).isTrue();
    }

    @Test @DisplayName("CPF formatado deve ser aceito")
    void deveAceitarCpfFormatado() {
        assertThat(CpfCnpjValidator.isValid("529.982.247-25")).isTrue();
    }

    @Test @DisplayName("CPF com dígitos iguais deve ser rejeitado")
    void deveRejeitarCpfDigitosIguais() {
        assertThat(CpfCnpjValidator.isValid("11111111111")).isFalse();
    }

    @Test @DisplayName("CPF com dígito inválido deve ser rejeitado")
    void deveRejeitarCpfInvalido() {
        assertThat(CpfCnpjValidator.isValid("12345678901")).isFalse();
    }

    @Test @DisplayName("CNPJ válido deve ser aceito")
    void deveAceitarCnpjValido() {
        assertThat(CpfCnpjValidator.isValid("11222333000181")).isTrue();
    }

    @Test @DisplayName("CNPJ formatado deve ser aceito")
    void deveAceitarCnpjFormatado() {
        assertThat(CpfCnpjValidator.isValid("11.222.333/0001-81")).isTrue();
    }

    @Test @DisplayName("CNPJ inválido deve ser rejeitado")
    void deveRejeitarCnpjInvalido() {
        assertThat(CpfCnpjValidator.isValid("11222333000199")).isFalse();
    }

    @Test @DisplayName("valor nulo deve ser rejeitado")
    void deveRejeitarNulo() {
        assertThat(CpfCnpjValidator.isValid(null)).isFalse();
    }

    @Test @DisplayName("valor com tamanho inesperado deve ser rejeitado")
    void deveRejeitarTamanhoInvalido() {
        assertThat(CpfCnpjValidator.isValid("12345")).isFalse();
    }

    @Test @DisplayName("tipo() retorna CPF para CPF válido")
    void tipoDeveRetornarCpf() {
        assertThat(CpfCnpjValidator.tipo("52998224725")).isEqualTo("CPF");
    }

    @Test @DisplayName("tipo() retorna CNPJ para CNPJ válido")
    void tipoDeveRetornarCnpj() {
        assertThat(CpfCnpjValidator.tipo("11222333000181")).isEqualTo("CNPJ");
    }

    @Test @DisplayName("tipo() retorna null para inválido")
    void tipoDeveRetornarNullParaInvalido() {
        assertThat(CpfCnpjValidator.tipo("12345")).isNull();
    }
}
