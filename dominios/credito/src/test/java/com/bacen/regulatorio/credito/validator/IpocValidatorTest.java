package com.bacen.regulatorio.credito.validator;

import com.bacen.regulatorio.credito.enums.TipoClienteIpoc;
import com.bacen.regulatorio.credito.valueobject.Ipoc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IpocValidatorTest {

    @Test
    @DisplayName("Circular 3.953 — IPOC montado a partir dos componentes deve seguir o exemplo do Doc 3040")
    void deveMontarIpocConformeExemploDoDocumento() {
        Ipoc ipoc = Ipoc.of(
                "00.001.234/0001-01",
                "0202",
                TipoClienteIpoc.PESSOA_FISICA_COM_CPF,
                "001.234.567-89",
                "abc78");

        assertThat(ipoc.valor()).isEqualTo("000012340202100123456789abc78");
    }

    @Test
    @DisplayName("Circular 3.953 — IPOC bruto válido deve ser aceito")
    void deveAceitarIpocValido() {
        assertThat(IpocValidator.validar("000012340202100123456789abc78")).isEmpty();
    }

    @Test
    @DisplayName("Circular 3.953 — IPOC com tipo de cliente inválido deve ser recusado")
    void deveRecusarTipoClienteInvalido() {
        assertThat(IpocValidator.validar("000012340202900123456789abc78"))
                .contains("Tipo de cliente deve ser um valor entre 1 e 6");
    }

    @Test
    @DisplayName("Circular 3.953 — IPOC com contrato vazio deve ser recusado")
    void deveRecusarContratoVazio() {
        assertThat(IpocValidator.validarComponentes(
                "00001234",
                "0202",
                TipoClienteIpoc.PESSOA_FISICA_COM_CPF,
                "00123456789",
                ""))
                .contains("Codigo do contrato nao pode ser vazio");
    }

    @Test
    @DisplayName("Circular 3.953 — tipo de cliente 3 deve completar o código com zeros à esquerda")
    void deveCompletarCodigoClienteParaTiposSemDocumento() {
        Ipoc ipoc = Ipoc.of(
                "00001234",
                "0215",
                TipoClienteIpoc.PESSOA_FISICA_NO_EXTERIOR,
                "XYZ123",
                "contrato1");

        assertThat(ipoc.valor()).startsWith("0000123402153");
        assertThat(ipoc.valor()).contains("00000000XYZ123");
    }

    @Test
    @DisplayName("Circular 3.953 — IPOC inválido no construtor deve lançar exceção")
    void deveLancarExcecaoParaIpocInvalido() {
        assertThatThrownBy(() -> new Ipoc("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("IPOC deve ter entre 22 e 67 caracteres");
    }
}
