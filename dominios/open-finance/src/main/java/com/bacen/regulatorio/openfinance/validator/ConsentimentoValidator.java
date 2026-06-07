package com.bacen.regulatorio.openfinance.validator;

import com.bacen.regulatorio.commons.validator.CpfCnpjValidator;
import com.bacen.regulatorio.openfinance.enums.PermissaoConsentimento;
import com.bacen.regulatorio.openfinance.enums.StatusConsentimento;
import com.bacen.regulatorio.openfinance.valueobject.Consentimento;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
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
        if (dataSolicitacao == null || dataExpiracao == null) {
            return Optional.of("Datas de solicitacao e expiracao sao obrigatorias");
        }
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
        if (status == null) {
            return Optional.of("Status do consentimento é obrigatório");
        }
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
        if (solicitadas == null || solicitadas.isEmpty()) {
            return Optional.of("Lista de permissoes solicitadas nao pode ser vazia");
        }
        if (concedidas == null || concedidas.isEmpty()) {
            return Optional.of("Lista de permissoes concedidas nao pode ser vazia");
        }
        if (solicitadas.stream().anyMatch(Objects::isNull) || concedidas.stream().anyMatch(Objects::isNull)) {
            return Optional.of("Listas de permissoes nao podem conter valores nulos");
        }
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
        if (permissoes == null || permissoes.isEmpty()) {
            return Optional.of("Lista de permissoes nao pode ser vazia");
        }
        if (permissoes.stream().anyMatch(Objects::isNull)) {
            return Optional.of("Lista de permissoes nao pode conter valores nulos");
        }
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

    /**
     * Valida a estrutura minima de um consentimento recebido pela aplicacao.
     */
    public static Optional<String> validarConsentimento(Consentimento consentimento) {
        if (consentimento == null) {
            return Optional.of("Consentimento nao pode ser nulo");
        }
        if (consentimento.consentId() == null || consentimento.consentId().isBlank()) {
            return Optional.of("Consentimento deve possuir consentId");
        }
        if (!CpfCnpjValidator.isValid(consentimento.cpfCnpjUsuario())) {
            return Optional.of("CPF/CNPJ do usuario invalido");
        }
        if (consentimento.status() == null) {
            return Optional.of("Status do consentimento e obrigatorio");
        }
        if (consentimento.permissoes() == null || consentimento.permissoes().isEmpty()) {
            return Optional.of("Consentimento deve possuir ao menos uma permissao");
        }
        if (consentimento.permissoes().stream().anyMatch(Objects::isNull)) {
            return Optional.of("Consentimento nao pode conter permissoes nulas");
        }
        return validarValidade(consentimento.dataExpiracao(), consentimento.dataCriacao());
    }

    /**
     * Valida se um consentimento pode ser usado para acessar as permissoes solicitadas.
     */
    public static Optional<String> validarAcesso(
            Consentimento consentimento,
            List<PermissaoConsentimento> solicitadas) {
        Optional<String> erro = validarConsentimento(consentimento);
        if (erro.isPresent()) {
            return erro;
        }

        erro = validarStatusParaUso(consentimento.status());
        if (erro.isPresent()) {
            return erro;
        }

        erro = validarPermissoes(solicitadas, consentimento.permissoes());
        if (erro.isPresent()) {
            return erro;
        }

        return validarDependenciasPermissoes(consentimento.permissoes());
    }
}
