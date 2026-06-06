# Recebiveis de Cartao

> Implementacao minima das regras regulatorias para negociacao de recebiveis de cartao conforme Resolucao 4.734/2019.

---

## Normas cobertas

| Norma | Status |
|-------|--------|
| Resolucao CMN 4.734/2019 | Coberta |
| Circular BCB 3.952/2019 | Coberta |
| Circular BCB 4.016/2020 | Coberta |
| Resolucao BCB 264/2022 | Coberta |

---

## Stack

| Item | Tecnologia |
|------|-----------|
| Runtime | Java 21 |
| Build | Maven (modulo do parent) |
| Testes | JUnit 5 + AssertJ |

---

## Estrutura

```
src/
├── main/java/com/bacen/regulatorio/recebiveis/
│   ├── enums/
│   │   ├── TipoEvento.java
│   │   ├── StatusNegociacao.java
│   │   ├── MotivoRecusa.java
│   │   └── IndicadorInteroperabilidade.java
│   ├── validator/
│   │   └── RecebivelValidator.java
│   └── valueobject/
│       └── UnidadeRecebivelId.java
└── test/java/com/bacen/regulatorio/recebiveis/validator/
    └── RecebivelValidatorTest.java
exemplos/
├── inclusao_request.json
├── inclusao_aceita.json
├── inclusao_recusada.json
└── cancelamento_aceita.json
docs/
└── normas.md
```

> Validacao de CPF/CNPJ delegada para `CpfCnpjValidator` no modulo `commons`.

---

## Regras implementadas

| Regra | Classe | Norma |
|-------|--------|-------|
| Prioridade unica por unidade ativa | `RecebivelValidator.validarPrioridade` | Res. 4.734 Art. 4 |
| Saldo disponivel >= valor do gravame | `RecebivelValidator.validarSaldo` | Res. 4.734 Art. 7 |
| Saldo recalculado em alteracao | `RecebivelValidator.validarSaldoAlteracao` | Res. 4.734 Art. 7 |
| Indicador de interoperabilidade SI/CI/NI | `RecebivelValidator.validarInteroperabilidade` | Circ. 4.016 Art. 2 IX |
| CPF com digitos verificadores | `commons.CpfCnpjValidator.isValid` | Circ. 4.016 |
| CNPJ com digitos verificadores | `commons.CpfCnpjValidator.isValid` | Circ. 4.016 |
| Chave unica SHA-256 da unidade | `UnidadeRecebivelId.chaveUnica` | Res. 4.734 Art. 3 |

---

## Uso como dependencia

```xml
<dependency>
    <groupId>com.bacen.regulatorio</groupId>
    <artifactId>bacen-regulatorio-recebiveis</artifactId>
    <version>1.1.0</version>
</dependency>
```

```java
// Validar antes de gravar um gravame
RecebivelValidator.validarPrioridade(prioridadeExistente)
    .or(() -> RecebivelValidator.validarSaldo(valorGarantia, saldoDisponivel))
    .or(() -> RecebivelValidator.validarInteroperabilidade(indicador))
    .or(() -> RecebivelValidator.validarCpfCnpj(cpfSolicitante, cnpjCredenciadora))
    .ifPresent(motivo -> { throw new RegraVioladaException(motivo); });
```

---

## Documentacao detalhada

Ver [docs/normas.md](docs/normas.md) para fluxos, conceitos e mapeamento norma-codigo.
