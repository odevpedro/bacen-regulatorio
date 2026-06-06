# Normas — DREX

## Normas cobertas

| Norma | Tema | Artigos |
|-------|------|---------|
| DREX — Real Digital | CBDC brasileiro (em desenvolvimento pelo BCB) | — |

> As regras do DREX ainda estão em desenvolvimento pelo BCB. Este módulo implementa
> os conceitos esperados com base nas diretrizes publicadas: atomic settlement,
> liquidez em tempo real, contratos inteligentes.

## Conceitos

| Conceito | Definição |
|----------|-----------|
| Real Digital (DREX) | CBDC (Central Bank Digital Currency) brasileiro |
| Atomic Settlement | Liquidação simultânea de ativo e pagamento (DvP) |
| Tokenização | Representação digital de ativos financeiros |

## Fluxo

```
Transação DREX
    └── DrexValidator.validarValorMinimo(valor)
    └── ReservaLiquidez.possuiLiquidez(valor)
    └── DrexValidator.validarLiquidez(reserva, transacao)
    └── DrexValidator.validarAtomicSettlement(reserva, transacao)
```

## Mapeamento norma-código

| Regra | Implementação |
|-------|---------------|
| Liquidez mínima para transação | `DrexValidator.validarLiquidez` |
| Atomic settlement | `DrexValidator.validarAtomicSettlement` |
| Saldo total (disponível + bloqueado) | `ReservaLiquidez.saldoTotal` |
| Verificação de liquidez | `ReservaLiquidez.possuiLiquidez` |
| Tipos de ativo digital | `TipoAtivoDigital` (enum) |
| Status de liquidação | `StatusLiquidacaoDrex` (enum) |
