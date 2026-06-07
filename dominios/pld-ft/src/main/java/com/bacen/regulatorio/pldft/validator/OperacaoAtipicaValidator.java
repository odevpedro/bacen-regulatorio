package com.bacen.regulatorio.pldft.validator;

import com.bacen.regulatorio.pldft.enums.NivelRiscoCliente;
import com.bacen.regulatorio.pldft.enums.TipoOperacaoAtipica;
import com.bacen.regulatorio.pldft.valueobject.PerfilRiscoCliente;

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
        if (valor == null) {
            return alertas;
        }
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
        if (valor == null || nivelRisco == null || limiteEsperado == null) {
            return false;
        }
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
        if (valores == null || valores.isEmpty()) {
            return false;
        }
        BigDecimal soma = valores.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        long operacoesAbaixoLimite = valores.stream()
                .filter(v -> v.compareTo(LIMITE_COMUNICACAO_COAF_ESPECIE) < 0)
                .count();
        return soma.compareTo(LIMITE_COMUNICACAO_COAF_ESPECIE) >= 0
                && operacoesAbaixoLimite == valores.size();
    }

    /**
     * Consolida os principais alertas de PLD/FT para uma operacao.
     */
    public static List<TipoOperacaoAtipica> avaliarOperacaoCompleta(
            PerfilRiscoCliente perfil,
            BigDecimal valor,
            BigDecimal limiteEsperado,
            List<BigDecimal> valoresRecentes,
            String pais) {
        List<TipoOperacaoAtipica> alertas = new ArrayList<>(avaliarOperacaoEspecie(valor));

        if (perfil != null) {
            if (perfil.isPep()) {
                alertas.add(TipoOperacaoAtipica.PEP);
            }
            if (isIncompativelComPerfil(valor, perfil.nivelEfetivo(), limiteEsperado)) {
                alertas.add(TipoOperacaoAtipica.INCOMPATIVEL_PERFIL);
            }
        }

        if (JurisdicaoAltoRiscoValidator.isAltoRisco(pais)) {
            alertas.add(TipoOperacaoAtipica.JURISDICAO_ALTO_RISCO);
        }

        if (isFracionamentoSuspeito(valoresRecentes, 24)) {
            alertas.add(TipoOperacaoAtipica.FRACIONAMENTO_SUSPEITO);
        }

        return alertas.stream().distinct().toList();
    }
}
