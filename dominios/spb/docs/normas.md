# Normas — SPB

## Normas cobertas

| Norma | Tema | Artigos |
|-------|------|---------|
| Lei 10.214/2001 | Sistema de Pagamentos Brasileiro | Art. 1-15 |

## Conceitos

| Conceito | Definição |
|----------|-----------|
| LBTR | Liquidação Bruta em Tempo Real — transferências de alto valor |
| STR | Sistema de Transferência de Reservas — reservas bancárias |
| SILOC | Sistema de Liquidação de Operações de Crédito |

## Fluxo

```
Mensagem SPB recebida
    └── SpbValidator.validarHorarioCorte(sistema, horario, diaSemana)
    └── SpbValidator.validarCompatibilidadeMensagem(tipo, sistema)
    └── SpbValidator.validarValorMinimo(valor, sistema)
```

## Mapeamento norma-código

| Regra | Implementação |
|-------|---------------|
| Horário de corte STR (17:00) | `SpbValidator.validarHorarioCorte` |
| Horário de corte LBTR (17:30) | `SpbValidator.validarHorarioCorte` |
| Horário de corte SILOC (17:00) | `SpbValidator.validarHorarioCorte` |
| SPB não opera em finais de semana | `SpbValidator.validarHorarioCorte` |
| Compatibilidade mensagem-sistema | `SpbValidator.validarCompatibilidadeMensagem` |
| Valor mínimo por sistema | `SpbValidator.validarValorMinimo` |
| Tipos de mensagem ISO 20022 | `TipoMensagemSPB` (enum) |
