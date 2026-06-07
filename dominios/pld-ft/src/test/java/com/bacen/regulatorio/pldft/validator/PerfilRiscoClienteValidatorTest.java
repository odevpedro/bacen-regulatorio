package com.bacen.regulatorio.pldft.validator;

import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;
import com.bacen.regulatorio.pldft.valueobject.PerfilRiscoCliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PerfilRiscoClienteValidatorTest {

    private final PerfilRiscoCliente perfilValido = new PerfilRiscoCliente(
            "52998224725",
            NivelRiscoCliente.NORMAL,
            false,
            List.of("BRASIL"),
            LocalDate.now().minusMonths(2),
            List.of("renda compativel"));

    @Test @DisplayName("Circ.3978 — perfil valido deve ser aceito")
    void deveAceitarPerfilValido() {
        assertThat(PerfilRiscoClienteValidator.validarPerfil(perfilValido)).isEmpty();
    }

    @Test @DisplayName("Circ.3978 — CPF/CNPJ invalido deve ser rejeitado")
    void deveRejeitarCpfInvalido() {
        var perfil = new PerfilRiscoCliente(
                "12345678900",
                NivelRiscoCliente.NORMAL,
                false,
                List.of("BRASIL"),
                LocalDate.now().minusMonths(2),
                List.of("renda compativel"));

        assertThat(PerfilRiscoClienteValidator.validarPerfil(perfil)).isPresent();
    }

    @Test @DisplayName("Circ.3978 — perfil REFORCADO sem motivos deve ser rejeitado")
    void deveRejeitarReforcadoSemMotivos() {
        var perfil = new PerfilRiscoCliente(
                "52998224725",
                NivelRiscoCliente.REFORCADO,
                false,
                List.of("BRASIL"),
                LocalDate.now().minusMonths(2),
                List.of());

        assertThat(PerfilRiscoClienteValidator.validarPerfil(perfil)).isPresent();
    }

    @Test @DisplayName("Circ.3978 — perfil vencido deve exigir revisao")
    void deveExigirRevisaoQuandoVencido() {
        var perfil = new PerfilRiscoCliente(
                "52998224725",
                NivelRiscoCliente.NORMAL,
                false,
                List.of("BRASIL"),
                LocalDate.now().minusYears(2),
                List.of("renda compativel"));

        assertThat(PerfilRiscoClienteValidator.validarPerfilVigente(perfil)).isPresent();
    }
}
