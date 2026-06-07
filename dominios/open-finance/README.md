# Open Finance

> Implementação mínima das regras de consentimento e permissões conforme Resolução BCB 32/2020.

---

## Normas cobertas

| Norma | Status |
|-------|--------|
| Resolução BCB 32/2020 | Coberta |
| Resolução BCB 57/2021 (iniciação) | Parcial — enum de permissão |
| Resolução BCB 316/2023 (Fase 4) | Parcial — enums |

---

## Stack

| Item | Tecnologia |
|------|-----------|
| Runtime | Java 21 |
| Build | Maven (módulo do parent) |
| Testes | JUnit 5 + AssertJ |

---

## Estrutura

```
src/
├── main/java/com/bacen/regulatorio/openfinance/
│   ├── enums/
│   │   ├── StatusConsentimento.java
│   │   ├── PermissaoConsentimento.java
│   │   └── FaseOpenFinance.java
│   ├── validator/
│   │   └── ConsentimentoValidator.java
│   └── valueobject/
│       └── Consentimento.java
└── test/java/com/bacen/regulatorio/openfinance/validator/
    └── ConsentimentoValidatorTest.java
exemplos/
├── consentimento_request.json
└── consentimento_response.json
docs/
└── normas.md
```

---

## Regras implementadas

| Regra | Classe | Norma |
|-------|--------|-------|
| Validação estrutural de consentimento | `ConsentimentoValidator.validarConsentimento` | Res. BCB 32 Art. 10 a 19 |
| Validade máxima de 12 meses | `ConsentimentoValidator.validarValidade` | Res. BCB 32 Art. 12° |
| Status deve ser AUTHORISED para uso | `ConsentimentoValidator.validarStatusParaUso` | Res. BCB 32 Art. 14° |
| Permissões concedidas >= solicitadas | `ConsentimentoValidator.validarPermissoes` | Res. BCB 32 Art. 13° |
| Dependências hierárquicas de permissões | `ConsentimentoValidator.validarDependenciasPermissoes` | Res. BCB 32 |
| Acesso a dados com consentimento válido | `ConsentimentoValidator.validarAcesso` | Res. BCB 32 Art. 10 a 19 |
| Verificação de consentimento ativo | `Consentimento.isAtivo` | Res. BCB 32 Art. 16° |

---

## Uso como dependência

```xml
<dependency>
    <groupId>com.bacen.regulatorio</groupId>
    <artifactId>bacen-regulatorio-openfinance</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
// Verificar se consentimento permite acesso a transações
Consentimento c = new Consentimento(id, cpf, AUTHORISED, permissoes, criacao, expiracao);
ConsentimentoValidator.validarAcesso(c, List.of(ACCOUNTS_TRANSACTIONS_READ))
        .ifPresent(erro -> { throw new PermissaoNegadaException(erro); });
```

---

## Documentação detalhada

Ver [docs/normas.md](docs/normas.md) para as fases de implementação, fluxo de consentimento e modelo FAPI.
