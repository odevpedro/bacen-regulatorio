package com.bacen.regulatorio.openfinance.validator;

import com.bacen.regulatorio.openfinance.enums.PermissaoConsentimento;
import com.bacen.regulatorio.openfinance.enums.StatusConsentimento;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Resolução BCB 32/2020 — Art. 10 a 19.
 * Regras de validação do ciclo de vida do consentimento no Open Finance.
 */
public final class ConsentimentoValidator {

    /** Validade mínima: 1 segundo. Máxima: 12 meses (Res. BCB 32 Art. 12). */
    public static final long VALIDADE_MAXIMA_MESES = 12L;

    private ConsentimentoValidator() {}

    /**
     * Valida que a data de expiração está no intervalo permitido.
     *
     * @param dataExpiracao    data de expiração informada
     * @param dataSolicitacao  data em que o consentimento foi criado
     * @return mensagem de erro ou Optional.empty() se válido
     */
    public static Optional<String> validarValidade(OffsetDateTime dataExpiracao, OffsetDateTime dataSolicitacao) {
        if (dataExpiracao.isBefore(dataSolicitacao) || dataExpiracao.isEqual(dataSolicitacao)) {
            return Optional.of("Data de expiração deve ser posterior à data de solicitação");
        }
        if (dataExpiracao.isAfter(dataSolicitacao.plusMonths(VALIDADE_MAXIMA_MESES))) {
            return Optional.of("Validade máxima do consentimento é de 12 meses");
        }
        return Optional.empty();
    }

    /**
     * Valida que o consentimento está no status correto para a operação solicitada.
     * Apenas consentimentos AUTHORISED permitem compartilhamento de dados.
     */
    public static Optional<String> validarStatusParaUso(StatusConsentimento status) {
        return status == StatusConsentimento.AUTHORISED
                ? Optional.empty()
                : Optional.of("Consentimento não está autorizado. Status atual: " + status);
    }

    /**
     * Valida que as permissões solicitadas são um subconjunto das permissões concedidas.
     */
    public static Optional<String> validarPermissoes(
            List<PermissaoConsentimento> solicitadas,
            List<PermissaoConsentimento> concedidas) {
        List<PermissaoConsentimento> naoAutorizadas = solicitadas.stream()
                .filter(p -> !concedidas.contains(p))
                .toList();
        return naoAutorizadas.isEmpty()
                ? Optional.empty()
                : Optional.of("Permissões não autorizadas: " + naoAutorizadas);
    }

    /**
     * Resolução BCB 32 — permissões de transações exigem a permissão de contas base.
     * Ex: ACCOUNTS_TRANSACTIONS_READ exige ACCOUNTS_READ.
     */
    public static Optional<String> validarDependenciasPermissoes(List<PermissaoConsentimento> permissoes) {
        if (permissoes.contains(PermissaoConsentimento.ACCOUNTS_TRANSACTIONS_READ)
                && !permissoes.contains(PermissaoConsentimento.ACCOUNTS_READ)) {
            return Optional.of("ACCOUNTS_TRANSACTIONS_READ requer ACCOUNTS_READ");
        }
        if (permissoes.contains(PermissaoConsentimento.ACCOUNTS_BALANCES_READ)
                && !permissoes.contains(PermissaoConsentimento.ACCOUNTS_READ)) {
            return Optional.of("ACCOUNTS_BALANCES_READ requer ACCOUNTS_READ");
        }
        if (permissoes.contains(PermissaoConsentimento.CREDIT_CARDS_ACCOUNTS_TRANSACTIONS_READ)
                && !permissoes.contains(PermissaoConsentimento.CREDIT_CARDS_ACCOUNTS_READ)) {
            return Optional.of("CREDIT_CARDS_ACCOUNTS_TRANSACTIONS_READ requer CREDIT_CARDS_ACCOUNTS_READ");
        }
        // Fase 4 — seguros: permissoes especificas requerem INSURANCE_PERSONAL_READ ou INSURANCE_BUSINESS_READ
        if (permissoes.contains(PermissaoConsentimento.INSURANCE_PERSONAL_AUTOMOBILE_READ)
                && !permissoes.contains(PermissaoConsentimento.INSURANCE_PERSONAL_READ)) {
            return Optional.of("INSURANCE_PERSONAL_AUTOMOBILE_READ requer INSURANCE_PERSONAL_READ");
        }
        if (permissoes.contains(PermissaoConsentimento.INSURANCE_PERSONAL_LIFE_READ)
                && !permissoes.contains(PermissaoConsentimento.INSURANCE_PERSONAL_READ)) {
            return Optional.of("INSURANCE_PERSONAL_LIFE_READ requer INSURANCE_PERSONAL_READ");
        }
        if (permissoes.contains(PermissaoConsentimento.INSURANCE_BUSINESS_LIABILITY_READ)
                && !permissoes.contains(PermissaoConsentimento.INSURANCE_BUSINESS_READ)) {
            return Optional.of("INSURANCE_BUSINESS_LIABILITY_READ requer INSURANCE_BUSINESS_READ");
        }
        // Fase 4 — previdencia: portabilidade requer entidade
        if (permissoes.contains(PermissaoConsentimento.PENSION_OPEN_ENTITY_PORTABILITY_READ)
                && !permissoes.contains(PermissaoConsentimento.PENSION_OPEN_ENTITY_READ)) {
            return Optional.of("PENSION_OPEN_ENTITY_PORTABILITY_READ requer PENSION_OPEN_ENTITY_READ");
        }
        return Optional.empty();
    }
}
