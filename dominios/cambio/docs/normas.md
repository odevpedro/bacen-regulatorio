# Normas — Câmbio

## Normas cobertas

| Norma | Tema | Artigos |
|-------|------|---------|
| Resolução BCB 277/2022 | Disciplina o mercado de câmbio | Art. 1-45 |
| Circular BCB 3.691/2013 | Prazos de liquidação de operações de câmbio | Art. 1-15 |

## Conceitos

| Conceito | Definição |
|----------|-----------|
| Taxa PTAX | Taxa de câmbio oficial divulgada pelo BCB, calculada com base nas cotações do mercado |
| Contrato de Câmbio | Instrumento que formaliza a operação de câmbio, registrado no e-CAC |
| e-CAC | Sistema eletrônico do BCB para registro de operações de câmbio |

## Fluxo

```
Operação de câmbio
    └── CambioValidator.validarMoedaElegivel(moeda)
    └── CambioValidator.validarValorMinimo(valor, moeda)
    └── CambioValidator.validarPrazoLiquidacao(finalidade, dias)
    └── CambioValidator.validarVariacaoTaxa(taxaContratual, taxaPtax, limite)
```

## Mapeamento norma-código

| Regra | Implementação |
|-------|---------------|
| Moeda de operação deve ser diferente de BRL | `CambioValidator.validarMoedaElegivel` |
| Prazo máximo por finalidade | `CambioValidator.validarPrazoLiquidacao` |
| Variação máxima da taxa contratual vs PTAX | `CambioValidator.validarVariacaoTaxa` |
| Contrato de câmbio com campos obrigatórios | `ContratoCambio` (record) |
| Taxa de câmbio PTAX ou contratual | `TaxaCambio` (record) |
