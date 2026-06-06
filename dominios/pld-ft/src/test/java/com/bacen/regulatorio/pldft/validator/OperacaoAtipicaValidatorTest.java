package com.bacen.regulatorio.pldft.validator;

import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;
import com.bacen.regulatorio.pldft.enums.TipoOperacaoAtipica;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OperacaoAtipicaValidatorTest {

    @Test @DisplayName("Circ.3978 — espécie abaixo de R$2.000 não gera alerta")
    void operacaoAbaixoLimiteNaoGeraAlerta() {
        List<TipoOperacaoAtipica> alertas = OperacaoAtipicaValidator.avaliarOperacaoEspecie(
                new BigDecimal("1999.99"));
        assertThat(alertas).isEmpty();
    }

    @Test @DisplayName("Circ.3978 — espécie acima de R$2.000 exige registro")
    void operacaoAcimaLimiteExigeRegistro() {
        List<TipoOperacaoAtipica> alertas = OperacaoAtipicaValidator.avaliarOperacaoEspecie(
                new BigDecimal("2000.00"));
        assertThat(alertas).contains(TipoOperacaoAtipica.ESPECIE_ACIMA_LIMITE);
        assertThat(alertas).doesNotContain(TipoOperacaoAtipica.ESPECIE_COMUNICACAO_COAF);
    }

    @Test @DisplayName("Circ.3978 — espécie acima de R$10.000 exige comunicação ao COAF")
    void operacaoAcimaLimiteCOAFExigeComunicacao() {
        List<TipoOperacaoAtipica> alertas = OperacaoAtipicaValidator.avaliarOperacaoEspecie(
                new BigDecimal("10000.00"));
        assertThat(alertas).contains(
                TipoOperacaoAtipica.ESPECIE_ACIMA_LIMITE,
                TipoOperacaoAtipica.ESPECIE_COMUNICACAO_COAF);
    }

    @Test @DisplayName("Circ.3978 — fracionamento suspeito detectado quando soma atinge limite")
    void deveDetectarFracionamento() {
        List<BigDecimal> operacoes = List.of(
                new BigDecimal("4000.00"),
                new BigDecimal("3500.00"),
                new BigDecimal("3000.00")
        );
        assertThat(OperacaoAtipicaValidator.isFracionamentoSuspeito(operacoes, 24)).isTrue();
    }

    @Test @DisplayName("Circ.3978 — operações legítimas não são classificadas como fracionamento")
    void naoDeveClassificarOperacoesLegitimas() {
        List<BigDecimal> operacoes = List.of(
                new BigDecimal("500.00"),
                new BigDecimal("300.00")
        );
        assertThat(OperacaoAtipicaValidator.isFracionamentoSuspeito(operacoes, 24)).isFalse();
    }

    @Test @DisplayName("Circ.3978 — cliente REFORCADO nunca é marcado como incompatível")
    void clienteReforcadoNaoEIncompativelComPerfil() {
        assertThat(OperacaoAtipicaValidator.isIncompativelComPerfil(
                new BigDecimal("9999999.00"),
                NivelRiscoCliente.REFORCADO,
                new BigDecimal("1000.00")
        )).isFalse();
    }
}
