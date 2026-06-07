package com.bacen.regulatorio.pix.validator;

import com.bacen.regulatorio.commons.validator.CpfCnpjValidator;
import com.bacen.regulatorio.pix.valueobject.MandatoPix;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Res. BCB 191/2022 — validacao de Pix Automatico (recorrencia).
 *
 * Regras:
 *   - Valor da cobranca nao pode exceder o valor maximo do mandato
 *   - Valor acumulado no mes nao pode exceder o limite mensal
 *   - Quantidade de cobrancas em um periodo nao pode exceder o limite
 *   - Mandato deve estar ATIVO e dentro da validade
 */
public final class MandatoPixValidator {

    private MandatoPixValidator() {}

    /**
     * Valida se o mandato esta apto para cobranca.
     */
    public static Optional<String> validarMandatoAtivo(MandatoPix mandato) {
        Optional<String> erro = validarMandato(mandato);
        if (erro.isPresent()) {
            return erro;
        }
        if (!mandato.isAtivo()) {
            return Optional.of("Mandato " + mandato.idMandato() + " nao esta ativo");
        }
        return Optional.empty();
    }

    /**
     * Valida se o valor da cobranca respeita o limite do mandato.
     */
    public static Optional<String> validarValorCobranca(MandatoPix mandato, BigDecimal valor) {
        if (mandato == null) {
            return Optional.of("Mandato nao pode ser nulo");
        }
        if (valor == null) {
            return Optional.of("Valor da cobranca nao pode ser nulo");
        }
        Optional<String> erro = validarMandato(mandato);
        if (erro.isPresent()) {
            return erro;
        }
        if (valor.compareTo(mandato.valorMaximo()) > 0) {
            return Optional.of("Valor " + valor + " excede o limite de " + mandato.valorMaximo() + " do mandato");
        }
        return Optional.empty();
    }

    /**
     * Valida o valor acumulado no mes contra o limite mensal do mandato.
     */
    public static Optional<String> validarValorAcumuladoMes(MandatoPix mandato, BigDecimal valorJaCobradoNoMes, BigDecimal novoValor) {
        if (mandato == null) {
            return Optional.of("Mandato nao pode ser nulo");
        }
        if (valorJaCobradoNoMes == null || novoValor == null) {
            return Optional.of("Valores acumulados nao podem ser nulos");
        }
        Optional<String> erro = validarMandato(mandato);
        if (erro.isPresent()) {
            return erro;
        }
        if (mandato.valorMaximoMes() == null) {
            return Optional.empty();
        }
        BigDecimal total = valorJaCobradoNoMes.add(novoValor);
        if (total.compareTo(mandato.valorMaximoMes()) > 0) {
            return Optional.of("Valor acumulado no mes " + total + " excede o limite de " + mandato.valorMaximoMes());
        }
        return Optional.empty();
    }

    /**
     * Valida a quantidade de cobrancas em relacao ao limite do mandato.
     */
    public static Optional<String> validarQuantidadeCobrancas(MandatoPix mandato, int quantidadeJaRealizada) {
        if (mandato == null) {
            return Optional.of("Mandato nao pode ser nulo");
        }
        Optional<String> erro = validarMandato(mandato);
        if (erro.isPresent()) {
            return erro;
        }
        if (quantidadeJaRealizada >= mandato.quantidadeMaxima()) {
            return Optional.of("Quantidade maxima de " + mandato.quantidadeMaxima()
                    + " cobrancas ja foi atingida para este mandato");
        }
        return Optional.empty();
    }

    /**
     * Valida a estrutura minima de um mandato de Pix Automatico.
     */
    public static Optional<String> validarMandato(MandatoPix mandato) {
        if (mandato == null) {
            return Optional.of("Mandato nao pode ser nulo");
        }
        if (mandato.idMandato() == null || mandato.idMandato().isBlank()) {
            return Optional.of("ID do mandato e obrigatorio");
        }
        if (!CpfCnpjValidator.isValid(mandato.cpfCnpjPagador())) {
            return Optional.of("CPF/CNPJ do pagador invalido");
        }
        if (!CpfCnpjValidator.isValid(mandato.cpfCnpjRecebedor())) {
            return Optional.of("CPF/CNPJ do recebedor invalido");
        }
        if (mandato.valorMaximo() == null || mandato.valorMaximo().compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Valor maximo deve ser positivo");
        }
        if (mandato.valorMaximoMes() != null && mandato.valorMaximoMes().compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Valor maximo mensal deve ser positivo quando informado");
        }
        if (mandato.quantidadeMaxima() <= 0) {
            return Optional.of("Quantidade maxima deve ser positiva");
        }
        if (mandato.periodicidade() == null) {
            return Optional.of("Periodicidade do mandato e obrigatoria");
        }
        if (mandato.status() == null) {
            return Optional.of("Status do mandato e obrigatorio");
        }
        if (mandato.dataInicio() == null || mandato.dataFim() == null) {
            return Optional.of("Datas de inicio e termino do mandato sao obrigatorias");
        }
        if (mandato.dataFim().isBefore(mandato.dataInicio())) {
            return Optional.of("Data de termino deve ser posterior a data de inicio");
        }
        return Optional.empty();
    }

    /**
     * Valida uma cobranca completa, combinando elegibilidade, valor e limite mensal.
     */
    public static Optional<String> validarCobranca(
            MandatoPix mandato,
            BigDecimal valor,
            BigDecimal valorJaCobradoNoMes,
            int quantidadeJaRealizada) {
        Optional<String> erro = validarMandatoAtivo(mandato);
        if (erro.isPresent()) {
            return erro;
        }

        erro = validarValorCobranca(mandato, valor);
        if (erro.isPresent()) {
            return erro;
        }

        erro = validarValorAcumuladoMes(mandato, valorJaCobradoNoMes, valor);
        if (erro.isPresent()) {
            return erro;
        }

        return validarQuantidadeCobrancas(mandato, quantidadeJaRealizada);
    }
}
