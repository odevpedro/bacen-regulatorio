# Normas — Crédito

## Normas cobertas

| Norma | Tema | Artigos |
|-------|------|---------|
| Resolução CMN 4.558/2017 | Disciplina operações de crédito e tarifas | Art. 1-30 |
| Resolução CMN 4.966/2021 | Atualiza regras de crédito consignado | Art. 1-20 |
| Circular BCB 3.953/2019 | Identificação padronizada de operações de crédito (IPOC) | Art. 1-4 |
| Instruções de Preenchimento do Doc 3040 | Composição e remessa do IPOC ao SCR | Item D.1.1 |

## Conceitos

| Conceito | Definição |
|----------|-----------|
| CET | Custo Efetivo Total — taxa anual que inclui juros, tarifas, tributos e seguros |
| CDC | Crédito Direto ao Consumidor — financiamento de bens e serviços |
| Crédito Rotativo | Modalidade onde o limite é renovado a cada pagamento |
| IPOC | Identificação Padronizada da Operação de Crédito usada para rastrear operações no SCR |

## Fluxo

```
Operação de crédito
    └── CreditoValidator.validarPrazoMaximo(tipo, prazoMeses)
    └── CreditoValidator.validarCET(cet, limiteLegal)
    └── CreditoValidator.validarValorMinimo(valor, tipo)
    └── Ipoc.of(cnpjInstituicao, modalidade, tipoCliente, codigoCliente, codigoContrato)
    └── IpocValidator.validar(ipoc)
```

## Mapeamento norma-código

| Regra | Implementação |
|-------|---------------|
| Prazo máximo de 60 meses para CDC | `CreditoValidator.validarPrazoMaximo` |
| Prazo máximo de 96 meses para crédito pessoal | `CreditoValidator.validarPrazoMaximo` |
| Prazo máximo de 420 meses para imobiliário | `CreditoValidator.validarPrazoMaximo` |
| CET obrigatório na contratação | `CreditoValidator.validarCET` |
| Cálculo do CET | `CET.calcular` |
| Representação de parcela | `Parcela` (record) |
| Composição e validação do IPOC | `Ipoc.of` / `IpocValidator.validar` |
