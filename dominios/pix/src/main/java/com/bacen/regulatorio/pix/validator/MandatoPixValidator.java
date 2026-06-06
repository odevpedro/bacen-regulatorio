package com.bacen.regulatorio.pix.validator;

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
        if (!mandato.isAtivo()) {
            return Optional.of("Mandato " + mandato.idMandato() + " nao esta ativo");
        }
        return Optional.empty();
    }

    /**
     * Valida se o valor da cobranca respeita o limite do mandato.
     */
    public static Optional<String> validarValorCobranca(MandatoPix mandato, BigDecimal valor) {
        if (valor.compareTo(mandato.valorMaximo()) > 0) {
            return Optional.of("Valor " + valor + " excede o limite de " + mandato.valorMaximo() + " do mandato");
        }
        return Optional.empty();
    }

    /**
     * Valida o valor acumulado no mes contra o limite mensal do mandato.
     */
    public static Optional<String> validarValorAcumuladoMes(MandatoPix mandato, BigDecimal valorJaCobradoNoMes, BigDecimal novoValor) {
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
        if (quantidadeJaRealizada >= mandato.quantidadeMaxima()) {
            return Optional.of("Quantidade maxima de " + mandato.quantidadeMaxima()
                    + " cobrancas ja foi atingida para este mandato");
        }
        return Optional.empty();
    }
}
