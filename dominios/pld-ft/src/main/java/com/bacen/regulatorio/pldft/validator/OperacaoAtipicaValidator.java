package com.bacen.regulatorio.pldft.validator;

import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;
import com.bacen.regulatorio.pldft.enums.TipoOperacaoAtipica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Circular BCB 3.978/2020 — monitoramento de operações atípicas.
 *
 * Limites que exigem ação obrigatória:
 *   - Espécie >= R$ 2.000: registro obrigatório
 *   - Espécie >= R$ 10.000 ou equivalente: comunicação ao COAF
 *   - Prazo de comunicação: até o dia útil seguinte à confirmação da suspeita
 */
public final class OperacaoAtipicaValidator {

    public static final BigDecimal LIMITE_REGISTRO_ESPECIE        = new BigDecimal("2000.00");
    public static final BigDecimal LIMITE_COMUNICACAO_COAF_ESPECIE = new BigDecimal("10000.00");

    private OperacaoAtipicaValidator() {}

    /**
     * Verifica se a operação com espécie exige alguma ação regulatória.
     *
     * @return lista de tipos de atenção que se aplicam (pode ser vazia se regular)
     */
    public static List<TipoOperacaoAtipica> avaliarOperacaoEspecie(BigDecimal valor) {
        List<TipoOperacaoAtipica> alertas = new ArrayList<>();
        if (valor.compareTo(LIMITE_REGISTRO_ESPECIE) >= 0) {
            alertas.add(TipoOperacaoAtipica.ESPECIE_ACIMA_LIMITE);
        }
        if (valor.compareTo(LIMITE_COMUNICACAO_COAF_ESPECIE) >= 0) {
            alertas.add(TipoOperacaoAtipica.ESPECIE_COMUNICACAO_COAF);
        }
        return alertas;
    }

    /**
     * Avalia se o valor de uma operação é incompatível com o perfil de risco.
     * Regra heurística — na prática a instituição deve usar seu modelo de scoring.
     *
     * @param valor          valor da operação
     * @param nivelRisco     perfil de risco do cliente
     * @param limiteEsperado teto esperado para o perfil (definido pela instituição)
     */
    public static boolean isIncompativelComPerfil(
            BigDecimal valor, NivelRiscoCliente nivelRisco, BigDecimal limiteEsperado) {
        if (nivelRisco == NivelRiscoCliente.REFORCADO) return false; // já está em reforçado
        return valor.compareTo(limiteEsperado) > 0;
    }

    /**
     * Circular 3.978/2020 — fracionamento suspeito.
     * Detecta padrão de múltiplas operações pequenas que somadas atingem o limite.
     *
     * @param valores      lista de valores de operações recentes do mesmo cliente
     * @param janelaHoras  janela de tempo considerada (ex: 24h)
     */
    public static boolean isFracionamentoSuspeito(List<BigDecimal> valores, int janelaHoras) {
        BigDecimal soma = valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        long operacoesAbaixoLimite = valores.stream()
                .filter(v -> v.compareTo(LIMITE_COMUNICACAO_COAF_ESPECIE) < 0)
                .count();
        return soma.compareTo(LIMITE_COMUNICACAO_COAF_ESPECIE) >= 0
                && operacoesAbaixoLimite == valores.size();
    }
}
