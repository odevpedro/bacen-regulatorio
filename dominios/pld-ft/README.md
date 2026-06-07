# PLD/FT — Prevenção à Lavagem de Dinheiro e Financiamento ao Terrorismo

> Implementação mínima das regras de monitoramento e comunicação ao COAF conforme Circular BCB 3.978/2020.

---

## Normas cobertas

| Norma | Status |
|-------|--------|
| Circular BCB 3.978/2020 | Coberta |
| Resolução BCB 277/2022 (PEP) | Coberta |
| Lei 9.613/1998 | Coberta — conceitos |

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
├── main/java/com/bacen/regulatorio/pldft/
│   ├── enums/
│   │   ├── NivelRiscoCliente.java
│   │   ├── TipoOperacaoAtipica.java
│   │   └── StatusComunicacaoCoaf.java
│   ├── validator/
│   │   ├── PerfilRiscoClienteValidator.java
│   │   └── OperacaoAtipicaValidator.java
│   └── valueobject/
│       └── PerfilRiscoCliente.java
└── test/java/com/bacen/regulatorio/pldft/validator/
    └── OperacaoAtipicaValidatorTest.java
exemplos/
└── operacao_atipica.json
docs/
└── normas.md
```

---

## Regras implementadas

| Regra | Classe | Norma |
|-------|--------|-------|
| Espécie >= R$2.000 exige registro | `OperacaoAtipicaValidator.avaliarOperacaoEspecie` | Circ. 3.978 Art. 36° |
| Espécie >= R$10.000 exige comunicação COAF | `OperacaoAtipicaValidator.avaliarOperacaoEspecie` | Circ. 3.978 Art. 37° |
| Detecção de fracionamento suspeito | `OperacaoAtipicaValidator.isFracionamentoSuspeito` | Circ. 3.978 Anexo I |
| Incompatibilidade com perfil econômico | `OperacaoAtipicaValidator.isIncompativelComPerfil` | Circ. 3.978 Art. 19° |
| Avaliação consolidada de alertas | `OperacaoAtipicaValidator.avaliarOperacaoCompleta` | Circ. 3.978 |
| Prazo de revisão de perfil por nível | `PerfilRiscoCliente.precisaRevisao` | Circ. 3.978 Art. 22° |
| PEP sempre eleva nível para REFORCADO | `PerfilRiscoCliente.nivelEfetivo` | Res. BCB 277/2022 |
| Validação estrutural de perfil | `PerfilRiscoClienteValidator.validarPerfil` | Circ. 3.978 |
| Validação de perfil vigente | `PerfilRiscoClienteValidator.validarPerfilVigente` | Circ. 3.978 |

---

## Limites de comunicação ao COAF

| Situação | Valor | Ação | Prazo |
|----------|-------|------|-------|
| Espécie | >= R$ 2.000 | Registro | Imediato |
| Espécie | >= R$ 10.000 | Comunicação COAF | Dia útil seguinte |
| Operação suspeita | Qualquer | Comunicação COAF | Dia útil seguinte |

---

## Uso como dependência

```xml
<dependency>
    <groupId>com.bacen.regulatorio</groupId>
    <artifactId>bacen-regulatorio-pldft</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
PerfilRiscoCliente perfil = new PerfilRiscoCliente(cpf, NORMAL, false, paises, dataRevisao, motivos);

// Avaliar uma operação em espécie
List<TipoOperacaoAtipica> alertas = OperacaoAtipicaValidator
    .avaliarOperacaoEspecie(new BigDecimal("12000.00"));
// → [ESPECIE_ACIMA_LIMITE, ESPECIE_COMUNICACAO_COAF]

// Avaliar um caso completo de PLD/FT
List<TipoOperacaoAtipica> alertasCompletos = OperacaoAtipicaValidator.avaliarOperacaoCompleta(
    perfil,
    new BigDecimal("12000.00"),
    new BigDecimal("1000.00"),
    historicoValores,
    "IRA");

// Verificar se perfil precisa de revisão
PerfilRiscoClienteValidator.validarPerfilVigente(perfil)
    .ifPresent(erro -> iniciarKycAtualizado(perfil));
```

---

## Documentação detalhada

Ver [docs/normas.md](docs/normas.md) para KYC, PEP, fracionamento suspeito e fluxo de comunicação ao COAF.
