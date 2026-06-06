package com.bacen.regulatorio.pldft.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PepValidatorTest {

    @Test @DisplayName("Circ.3978 — cargo PEP reconhecido")
    void deveReconhecerCargoPep() {
        assertThat(PepValidator.isCargoPep("MINISTRO_ESTADO")).isTrue();
        assertThat(PepValidator.isCargoPep("SENADOR")).isTrue();
    }

    @Test @DisplayName("Circ.3978 — cargo nao PEP deve retornar falso")
    void deveRejeitarCargoNaoPep() {
        assertThat(PepValidator.isCargoPep("ANALISTA")).isFalse();
        assertThat(PepValidator.isCargoPep(null)).isFalse();
    }

    @Test @DisplayName("Res.BCB277 — PEP ativo exige monitoramento")
    void pepAtivoExigeMonitoramento() {
        assertThat(PepValidator.validarCondicaoPep("52998224725", "SENADOR", 0)).isPresent();
    }

    @Test @DisplayName("Res.BCB277 — PEP apos 5 anos do termino nao exige mais monitoramento")
    void pepAposCincoAnosNaoExigeMonitoramento() {
        assertThat(PepValidator.validarCondicaoPep("52998224725", "SENADOR", 6)).isEmpty();
    }
}
