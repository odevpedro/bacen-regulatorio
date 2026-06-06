package com.bacen.regulatorio.pix.valueobject;

import com.bacen.regulatorio.pix.enums.PeriodicidadePixAutomatico;
import com.bacen.regulatorio.pix.enums.StatusMandatoPix;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Res. BCB 191/2022 — mandato de Pix Automatico.
 * Autorizacao do pagador para que o recebedor inicie cobrancas recorrentes.
 *
 * @param idMandato         identificador unico do mandato
 * @param cpfCnpjPagador    CPF/CNPJ do pagador
 * @param cpfCnpjRecebedor  CPF/CNPJ do recebedor
 * @param valorMaximo       valor maximo por cobranca
 * @param valorMaximoMes    valor maximo acumulado no mes
 * @param quantidadeMaxima  quantidade maxima de cobrancas no periodo
 * @param periodicidade     periodicidade das cobrancas
 * @param dataInicio        data de inicio do mandato
 * @param dataFim           data de termino do mandato
 * @param status            status do mandato
 */
public record MandatoPix(
        String idMandato,
        String cpfCnpjPagador,
        String cpfCnpjRecebedor,
        BigDecimal valorMaximo,
        BigDecimal valorMaximoMes,
        int quantidadeMaxima,
        PeriodicidadePixAutomatico periodicidade,
        LocalDate dataInicio,
        LocalDate dataFim,
        StatusMandatoPix status
) {
    public MandatoPix {
        if (idMandato == null || idMandato.isBlank()) {
            throw new IllegalArgumentException("ID do mandato e obrigatorio");
        }
        if (valorMaximo != null && valorMaximo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor maximo deve ser positivo");
        }
        if (quantidadeMaxima <= 0) {
            throw new IllegalArgumentException("Quantidade maxima deve ser positiva");
        }
        if (dataFim != null && dataInicio != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de termino deve ser posterior a data de inicio");
        }
    }

    public boolean isAtivo() {
        return status == StatusMandatoPix.ATIVO
                && (dataFim == null || !LocalDate.now().isAfter(dataFim));
    }
}
