package com.bacen.regulatorio.arranjos.validator;

import com.bacen.regulatorio.arranjos.enums.CodigoArranjo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ArranjoValidatorTest {

    @Test @DisplayName("Res.CMN 4.282 — código VCC deve ser reconhecido")
    void deveAceitarCodigoValido() {
        assertThat(ArranjoValidator.validarCodigo("VCC")).isEmpty();
        assertThat(ArranjoValidator.validarCodigo("vcc")).isEmpty();
        assertThat(ArranjoValidator.validarCodigo("PIX")).isEmpty();
        assertThat(ArranjoValidator.validarCodigo("MCD")).isEmpty();
    }

    @Test @DisplayName("Res.CMN 4.282 — código desconhecido deve retornar erro")
    void deveRecusarCodigoDesconhecido() {
        Optional<String> resultado = ArranjoValidator.validarCodigo("XYZ");
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).contains("XYZ");
    }

    @Test @DisplayName("Res.CMN 4.282 — código nulo deve retornar erro")
    void deveRecusarCodigoNulo() {
        assertThat(ArranjoValidator.validarCodigo(null)).isPresent();
    }

    @Test @DisplayName("Res.BCB 150/2021 — arranjo de crédito é elegível para recebíveis")
    void deveAceitarArranjoCredito() {
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.VCC)).isEmpty();
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.MCC)).isEmpty();
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.ECC)).isEmpty();
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.HCC)).isEmpty();
    }

    @Test @DisplayName("Res.BCB 150/2021 — PIX não é válido para recebíveis de cartão")
    void deveRecusarPixParaRecebiveis() {
        Optional<String> resultado = ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.PIX);
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).contains("PIX");
    }

    @Test @DisplayName("Res.BCB 150/2021 — TED e DOC não são válidos para recebíveis de cartão")
    void deveRecusarTedDocParaRecebiveis() {
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.TED)).isPresent();
        assertThat(ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.DOC)).isPresent();
    }

    @Test @DisplayName("Res.CMN 4.282 — arranjo de crédito é elegível para parcelamento")
    void deveAceitarArrangoCreditoParaParcelamento() {
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.VCC)).isEmpty();
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.MCF)).isEmpty();
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.ECF)).isEmpty();
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.ACF)).isEmpty();
    }

    @Test @DisplayName("Res.CMN 4.282 — débito não suporta crédito parcelado")
    void deveRecusarDebitoParaParcelamento() {
        Optional<String> resultado = ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.VCD);
        assertThat(resultado).isPresent();
        assertThat(resultado.get()).contains("débito");
    }

    @Test @DisplayName("Res.CMN 4.282 — pré-pago não suporta crédito parcelado")
    void deveRecusarPrePagoParaParcelamento() {
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.VPS)).isPresent();
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.MPS)).isPresent();
        assertThat(ArranjoValidator.validarElegibilidadeCredito(CodigoArranjo.EPS)).isPresent();
    }

    @Test @DisplayName("CodigoArranjo — classificação crédito/débito/pré-pago")
    void deveClassificarCorretamente() {
        assertThat(CodigoArranjo.VCC.isCredito()).isTrue();
        assertThat(CodigoArranjo.VCC.isDebito()).isFalse();
        assertThat(CodigoArranjo.VCC.isPrePago()).isFalse();

        assertThat(CodigoArranjo.MCD.isDebito()).isTrue();
        assertThat(CodigoArranjo.MCD.isCredito()).isFalse();

        assertThat(CodigoArranjo.EPS.isPrePago()).isTrue();
        assertThat(CodigoArranjo.EPS.isCredito()).isFalse();
    }
}
