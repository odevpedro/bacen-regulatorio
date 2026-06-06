package com.bacen.regulatorio.recebiveis.validator;

import com.bacen.regulatorio.commons.validator.CpfCnpjValidator;
import com.bacen.regulatorio.recebiveis.enums.IndicadorInteroperabilidade;
import com.bacen.regulatorio.recebiveis.enums.MotivoRecusa;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Resolução 4.734/2019 + Circular 3.952/2019 — regras de validação da registradora.
 *
 * Cada método retorna Optional.empty() quando a regra passa,
 * ou Optional.of(MotivoRecusa) quando a regra falha.
 * Isso permite compor validações em cadeia sem exceções.
 */
public final class RecebivelValidator {

    private RecebivelValidator() {}

    /**
     * Res. 4.734 Art. 4° — prioridade deve ser única por unidade ativa.
     *
     * @param prioridadeJaExiste true se já existe gravame ativo com essa prioridade na unidade
     */
    public static Optional<MotivoRecusa> validarPrioridade(boolean prioridadeJaExiste) {
        return prioridadeJaExiste ? Optional.of(MotivoRecusa.PRIORIDADE_DUPLICADA) : Optional.empty();
    }

    /**
     * Res. 4.734 Art. 7° — valor do gravame não pode superar o saldo disponível.
     *
     * @param valorGarantia   valor solicitado para o gravame
     * @param saldoDisponivel saldo atual da unidade de recebível
     */
    public static Optional<MotivoRecusa> validarSaldo(BigDecimal valorGarantia, BigDecimal saldoDisponivel) {
        if (valorGarantia == null || saldoDisponivel == null) return Optional.of(MotivoRecusa.ERRO_INTERNO);
        return valorGarantia.compareTo(saldoDisponivel) > 0
                ? Optional.of(MotivoRecusa.SALDO_INSUFICIENTE)
                : Optional.empty();
    }

    /**
     * Circular 4.016/2020 Art. 2° IX — indicador de interoperabilidade deve ser SI, CI ou NI.
     */
    public static Optional<MotivoRecusa> validarInteroperabilidade(String indicador) {
        try {
            IndicadorInteroperabilidade.valueOf(indicador);
            return Optional.empty();
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.of(MotivoRecusa.INTEROPERABILIDADE_INVALIDA);
        }
    }

    /**
     * Circular 4.016/2020 — CPF e CNPJ com dígitos verificadores válidos.
     */
    public static Optional<MotivoRecusa> validarCpfCnpj(String... valores) {
        for (String v : valores) {
            if (!CpfCnpjValidator.isValid(v)) return Optional.of(MotivoRecusa.CPF_CNPJ_INVALIDO);
        }
        return Optional.empty();
    }

    /** Delegado para CpfCnpjValidator.isValid — compatibilidade. */
    public static boolean isValidCpfCnpj(String value) {
        return CpfCnpjValidator.isValid(value);
    }

    /**
     * Regra de negócio para alteração — saldo efetivo após devolver o valor antigo.
     *
     * @param novoValor        novo valor de garantia
     * @param valorAtual       valor do gravame sendo alterado
     * @param saldoDisponivel  saldo atual (sem contar o gravame sendo alterado)
     */
    public static Optional<MotivoRecusa> validarSaldoAlteracao(
            BigDecimal novoValor, BigDecimal valorAtual, BigDecimal saldoDisponivel) {
        BigDecimal saldoRecuperado = saldoDisponivel.add(valorAtual);
        return novoValor.compareTo(saldoRecuperado) > 0
                ? Optional.of(MotivoRecusa.SALDO_INSUFICIENTE)
                : Optional.empty();
    }
}
