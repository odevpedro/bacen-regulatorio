# Evidencias - Open Finance

## Escopo

Base documental para consentimento, permissao granular e dependencias hierarquicas entre escopos.

## Evidencias primarias esperadas

| Norma | Nome de cache sugerido | Fonte oficial |
|-------|------------------------|---------------|
| Resolução BCB 32/2020 | `pdf/2020-10-29_res-bcb-32-open-finance.pdf` | Portal de normativos do BACEN |
| Resolução BCB 57/2021 | `pdf/2021-01-26_res-bcb-57-iniciacao.pdf` | Portal de normativos do BACEN |
| Resolução BCB 97/2021 | `pdf/2021-04-29_res-bcb-97-cambio-open-finance.pdf` | Portal de normativos do BACEN |
| Resolução BCB 316/2023 | `pdf/2023-07-27_res-bcb-316-open-finance-fase4.pdf` | Portal de normativos do BACEN |
| Instrução Normativa BCB 266/2022 | `pdf/2022-xx-xx_in-bcb-266-open-finance-api.pdf` | Portal de normativos do BACEN |

## Regras associadas

- validade maxima do consentimento
- status para uso
- permissao concedida >= solicitada
- dependencias hierarquicas

## Uso no codigo

- `ConsentimentoValidator`
- `Consentimento`
- enums de status, permissao e fase
