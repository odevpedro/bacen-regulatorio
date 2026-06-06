package com.bacen.regulatorio.openfinance.validator;

import com.bacen.regulatorio.openfinance.enums.PermissaoConsentimento;
import com.bacen.regulatorio.openfinance.enums.StatusConsentimento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConsentimentoValidatorTest {

    private final OffsetDateTime agora = OffsetDateTime.now();

    @Test @DisplayName("Res.BCB32 — validade dentro de 12 meses deve ser aceita")
    void deveAceitarValidadeDentroDoLimite() {
        assertThat(ConsentimentoValidator.validarValidade(
                agora.plusMonths(6), agora)).isEmpty();
    }

    @Test @DisplayName("Res.BCB32 — validade acima de 12 meses deve ser rejeitada")
    void deveRejeitarValidadeAcimaDoLimite() {
        assertThat(ConsentimentoValidator.validarValidade(
                agora.plusMonths(13), agora)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 — data de expiração no passado deve ser rejeitada")
    void deveRejeitarDataNoPassado() {
        assertThat(ConsentimentoValidator.validarValidade(
                agora.minusDays(1), agora)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 — consentimento AUTHORISED permite uso")
    void devePermitirUsoDeConsentimentoAuthorised() {
        assertThat(ConsentimentoValidator.validarStatusParaUso(
                StatusConsentimento.AUTHORISED)).isEmpty();
    }

    @Test @DisplayName("Res.BCB32 — consentimento REVOKED não permite uso")
    void deveBloquearUsoDeConsentimentoRevogado() {
        assertThat(ConsentimentoValidator.validarStatusParaUso(
                StatusConsentimento.REVOKED)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 — TRANSACTIONS_READ sem ACCOUNTS_READ deve falhar")
    void deveRejeitarTransactionsReadSemAccountsRead() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.ACCOUNTS_TRANSACTIONS_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 — TRANSACTIONS_READ com ACCOUNTS_READ deve passar")
    void deveAceitarTransactionsReadComAccountsRead() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.ACCOUNTS_READ,
                PermissaoConsentimento.ACCOUNTS_TRANSACTIONS_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isEmpty();
    }

    @Test @DisplayName("Res.BCB32 Fase4 — seguro auto requer INSURANCE_PERSONAL_READ")
    void deveRejeitarSeguroAutoSemPersonal() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.INSURANCE_PERSONAL_AUTOMOBILE_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 Fase4 — seguro auto com base deve passar")
    void deveAceitarSeguroAutoComPersonal() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.INSURANCE_PERSONAL_READ,
                PermissaoConsentimento.INSURANCE_PERSONAL_AUTOMOBILE_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isEmpty();
    }

    @Test @DisplayName("Res.BCB32 Fase4 — portabilidade requer previdencia base")
    void deveRejeitarPortabilidadeSemBase() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.PENSION_OPEN_ENTITY_PORTABILITY_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isPresent();
    }

    @Test @DisplayName("Res.BCB32 Fase4 — portabilidade com base deve passar")
    void deveAceitarPortabilidadeComBase() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.PENSION_OPEN_ENTITY_READ,
                PermissaoConsentimento.PENSION_OPEN_ENTITY_PORTABILITY_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isEmpty();
    }

    @Test @DisplayName("Res.BCB32 Fase4 — multiple permissoes aninhadas devem passar")
    void deveAceitarMultiplasPermissoesAninhadas() {
        List<PermissaoConsentimento> permissoes = List.of(
                PermissaoConsentimento.ACCOUNTS_READ,
                PermissaoConsentimento.ACCOUNTS_TRANSACTIONS_READ,
                PermissaoConsentimento.INSURANCE_PERSONAL_READ,
                PermissaoConsentimento.INSURANCE_PERSONAL_LIFE_READ,
                PermissaoConsentimento.PENSION_OPEN_ENTITY_READ,
                PermissaoConsentimento.PENSION_OPEN_ENTITY_PORTABILITY_READ);
        assertThat(ConsentimentoValidator.validarDependenciasPermissoes(permissoes)).isEmpty();
    }
}
