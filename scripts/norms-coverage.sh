#!/bin/bash
# Script para levantar quais normas regulatorias estao cobertas vs vigentes.
# Gera relatorio Markdown em docs/normas-cobertura.md
#
# Uso: ./scripts/norms-coverage.sh

set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUTPUT="$ROOT/docs/normas-cobertura.md"

echo "# Cobertura de Normas Regulatorias — BACEN" > "$OUTPUT"
echo "" >> "$OUTPUT"
echo "> Relatorio gerado em $(date +%Y-%m-%d)" >> "$OUTPUT"
echo "> Gerado por: scripts/norms-coverage.sh" >> "$OUTPUT"
echo "" >> "$OUTPUT"
echo "## Legenda" >> "$OUTPUT"
echo "" >> "$OUTPUT"
echo "| Simbolo | Significado |" >> "$OUTPUT"
echo "|---------|-------------|" >> "$OUTPUT"
echo "| Coberta | Norma implementada no codigo |" >> "$OUTPUT"
echo "| Parcial | Norma parcialmente coberta |" >> "$OUTPUT"
echo "| Nao coberta | Norma identificada mas nao implementada |" >> "$OUTPUT"
echo "" >> "$OUTPUT"

TOTAL=0
COBERTA=0
PARCIAL=0
NAO_COBERTA=0

declare -A DOMINIOS
DOMINIOS["commons"]="Utilitarios Compartilhados"
DOMINIOS["recebiveis-cartao"]="Recebiveis de Cartao"
DOMINIOS["pix"]="PIX"
DOMINIOS["open-finance"]="Open Finance"
DOMINIOS["arranjos-pagamento"]="Arranjos de Pagamento"
DOMINIOS["pld-ft"]="PLD/FT"
DOMINIOS["cambio"]="Cambio"
DOMINIOS["credito"]="Credito"
DOMINIOS["spb"]="SPB"
DOMINIOS["drex"]="DREX"

for DOMINIO in "${!DOMINIOS[@]}"; do
    NOME="${DOMINIOS[$DOMINIO]}"
    NORMA_FILE="$ROOT/dominios/$DOMINIO/docs/normas.md"

    echo "" >> "$OUTPUT"
    echo "### $NOME" >> "$OUTPUT"
    echo "" >> "$OUTPUT"
    echo "| Norma | Cobertura | Artigos |" >> "$OUTPUT"
    echo "|-------|-----------|---------|" >> "$OUTPUT"

    if [ -f "$NORMA_FILE" ]; then
        # Extrai linhas com normas da tabela no docs/normas.md
        # Formato esperado: | CODIGO | descricao | artigos |
        grep -E "^\|.*\|" "$NORMA_FILE" | grep -v "^| Norma" | grep -v "^|---" | while IFS='|' read -r _ norma _; do
            NORMA=$(echo "$norma" | xargs)
            if [ -n "$NORMA" ]; then
                COBERTURA="Coberta"
                if echo "$NORMA" | grep -qi "parcial"; then
                    COBERTURA="Parcial"
                    ((PARCIAL++)) || true
                else
                    ((COBERTA++)) || true
                fi
                ((TOTAL++)) || true
                echo "| $NORMA | $COBERTURA | |" >> "$OUTPUT"
            fi
        done
    else
        echo "| Nenhuma norma documentada | Nao coberta | |" >> "$OUTPUT"
        ((NAO_COBERTA++)) || true
        ((TOTAL++)) || true
    fi
done

echo "" >> "$OUTPUT"
echo "---" >> "$OUTPUT"
echo "## Resumo" >> "$OUTPUT"
echo "" >> "$OUTPUT"
echo "| Metrica | Valor |" >> "$OUTPUT"
echo "|---------|-------|" >> "$OUTPUT"
echo "| Total de normas mapeadas | $TOTAL |" >> "$OUTPUT"
echo "| Cobertas | $COBERTA |" >> "$OUTPUT"
echo "| Parciais | $PARCIAL |" >> "$OUTPUT"
echo "| Nao cobertas | $NAO_COBERTA |" >> "$OUTPUT"
echo "" >> "$OUTPUT"
echo "---" >> "$OUTPUT"
echo "_Relatorio gerado automaticamente por scripts/norms-coverage.sh_" >> "$OUTPUT"

echo "Relatorio gerado: $OUTPUT"
echo "Total: $TOTAL | Cobertas: $COBERTA | Parciais: $PARCIAL | Nao cobertas: $NAO_COBERTA"
