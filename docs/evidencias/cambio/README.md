# Evidencias - Cambio

## Escopo

Base documental para regras de moeda, prazo de liquidacao e variacao de taxa.

## Evidencias primarias esperadas

| Norma | Nome de cache sugerido | Fonte oficial |
|-------|------------------------|---------------|
| Resolução BCB 277/2022 | `pdf/2022-03-31_res-bcb-277-cambio.pdf` | Portal de normativos do BACEN |
| Circular BCB 3.691/2013 | `pdf/2013-01-08_circ-bcb-3691-prazo-cambio.pdf` | Portal de normativos do BACEN |

## Regras associadas

- moeda de origem diferente de BRL
- prazo maximo por finalidade
- variacao de taxa contratual versus PTAX
- contrato de cambio

## Uso no codigo

- `CambioValidator`
- `ContratoCambio`
- `TaxaCambio`
- enums de moeda e finalidade
