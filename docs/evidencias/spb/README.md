# Evidencias - SPB

## Escopo

Base documental para o Sistema de Pagamentos Brasileiro, horarios de corte e compatibilidade de mensagens.

## Evidencias primarias esperadas

| Norma | Nome de cache sugerido | Fonte oficial |
|-------|------------------------|---------------|
| Lei 10.214/2001 | `pdf/2001-03-27_lei-10214-spb.pdf` | Planalto / BACEN |

## Regras associadas

- horario de corte por sistema
- operacao em dia util
- compatibilidade entre tipo de mensagem e sistema
- valor minimo por sistema

## Uso no codigo

- `SpbValidator`
- `MensagemSPB`
- enums de sistema, status e tipo de mensagem
