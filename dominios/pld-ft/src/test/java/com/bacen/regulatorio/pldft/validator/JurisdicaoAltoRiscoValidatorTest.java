package com.bacen.regulatorio.pldft.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JurisdicaoAltoRiscoValidatorTest {

    @Test @DisplayName("Circ.3978 — pais de alto risco detectado")
    void deveDetectarAltoRisco() {
        assertThat(JurisdicaoAltoRiscoValidator.isAltoRisco("COREEIA_DO_NORTE")).isTrue();
        assertThat(JurisdicaoAltoRiscoValidator.isAltoRisco("IRA")).isTrue();
    }

    @Test @DisplayName("Circ.3978 — pais de baixo risco nao deve ser detectado")
    void naoDeveDetectarBaixoRisco() {
        assertThat(JurisdicaoAltoRiscoValidator.isAltoRisco("BRASIL")).isFalse();
    }

    @Test @DisplayName("Circ.3978 — pais monitorado detectado")
    void deveDetectarMonitorado() {
        assertThat(JurisdicaoAltoRiscoValidator.isMonitorada("NIGERIA")).isTrue();
    }

    @Test @DisplayName("Circ.3978 — avaliarJurisdicao retorna alerta para alto risco")
    void avaliarJurisdicaoAltoRiscoRetornaAlerta() {
        var resultado = JurisdicaoAltoRiscoValidator.avaliarJurisdicao("IRA");
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).contains("alto risco");
    }

    @Test @DisplayName("Circ.3978 — avaliarJurisdicao retorna alerta para monitorada")
    void avaliarJurisdicaoMonitoradaRetornaAlerta() {
        var resultado = JurisdicaoAltoRiscoValidator.avaliarJurisdicao("TURQUIA");
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).contains("monitorada");
    }

    @Test @DisplayName("Circ.3978 — avaliarJurisdicao retorna vazio para pais seguro")
    void avaliarJurisdicaoSeguraRetornaVazio() {
        assertThat(JurisdicaoAltoRiscoValidator.avaliarJurisdicao("BRASIL")).isEmpty();
    }
}
