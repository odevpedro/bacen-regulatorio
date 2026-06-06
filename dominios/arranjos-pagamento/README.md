# Arranjos de Pagamento

> Catálogo de códigos e regras dos arranjos de pagamento regulados pelo BACEN conforme Resolução CMN 4.282/2013.

---

## Normas cobertas

| Norma | Status |
|-------|--------|
| Resolução CMN 4.282/2013 | Coberta |
| Lei 12.865/2013 | Coberta — conceitos |
| Resolução BCB 150/2021 | Parcial — critérios de vigilância |

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
├── main/java/com/bacen/regulatorio/arranjos/
│   ├── enums/
│   │   ├── CodigoArranjo.java
│   │   └── TipoParticipante.java
│   └── validator/
│       └── ArranjoValidator.java
└── test/java/com/bacen/regulatorio/arranjos/validator/
    └── ArranjoValidatorTest.java
exemplos/
└── participante_arranjo.json
docs/
└── normas.md
```

---

## Regras implementadas

| Regra | Classe | Norma |
|-------|--------|-------|
| Código de arranjo registrado no BCB | `ArranjoValidator.validarCodigo` | Res. CMN 4.282 |
| Arranjo elegível para recebíveis de cartão | `ArranjoValidator.validarArranjoParaRecebiveis` | Res. BCB 150/2021 |
| Arranjo elegível para crédito parcelado | `ArranjoValidator.validarElegibilidadeCredito` | Res. CMN 4.282 |
| Classificação crédito/débito/pré-pago | `CodigoArranjo.isCredito/isDebito/isPrePago` | SPB |

---

## Uso como dependência

```xml
<dependency>
    <groupId>com.bacen.regulatorio</groupId>
    <artifactId>bacen-regulatorio-arranjos</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
// Validar arranjo antes de criar unidade de recebível
ArranjoValidator.validarArranjoParaRecebiveis(CodigoArranjo.VCC)
    .ifPresent(erro -> { throw new ArranjoInvalidoException(erro); });
```

---

## Documentação detalhada

Ver [docs/normas.md](docs/normas.md) para papéis dos participantes, taxa de intercâmbio e critérios de vigilância.
