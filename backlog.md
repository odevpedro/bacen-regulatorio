# Backlog — bacen-regulatorio

> Registro vivo do progresso do projeto. Atualizado a cada mudança de estado de uma funcionalidade.
> **Ultima atualizacao:** 2026-06-03

---

## Sobre o Projeto

Referência regulatória BACEN com implementações mínimas, fluxos e exemplos organizados por domínio normativo. Cada domínio é um módulo Maven independente com enums, value objects, validators e testes que servem como documentação executável das normas.

**Versao atual:** `1.2.0`
**Stack principal:** Java 21, Maven multi-módulo, JUnit 5, AssertJ

---

## Legenda

| Simbolo | Significado |
|---------|-------------|
| `[ ]`   | Pendente |
| `[~]`   | Em andamento |
| `[x]`   | Concluido |
| `P0`    | Critico |
| `P1`    | Alta prioridade |
| `P2`    | Media prioridade |
| `P3`    | Melhoria |
| `XS` `S` `M` `L` `XL` | Estimativa de complexidade |

---

## Em Andamento

_Nenhum item em andamento._

---

## Pendentes

### Novos domínios

- `[ ]` `P2` `L` Investimentos — CRI, CRA, LCI, LCA (ICVM 476, 400)
- `[ ]` `P3` `S` Seguros — SUSEP + BACEN (dados no Open Finance Fase 4)

### Melhorias nos domínios existentes

- `[ ]` `P2` `M` Open Finance: validador de JWT/FAPI (mTLS + PKCE)
- `[ ]` `P3` `S` Arranjos: critérios de vigilância (volume > 1bi ou > R$500bi)
- `[ ]` `P3` `S` Badge de cobertura de testes no README raiz

### Infraestrutura do repositório

---

## Concluidas

| Item | Data | Descricao |
|------|------|-----------|
| Dominio: Cambio | 2026-06-03 | 4 enums, 2 VOs, 1 validator, 9 testes, docs (Res. BCB 277, Circ. 3.691) |
| Dominio: Credito | 2026-06-03 | 4 enums, 2 VOs, 1 validator, 8 testes, docs (Res. 4.558, 4.966) |
| Dominio: SPB | 2026-06-03 | 3 enums, 1 VO, 1 validator, 7 testes, docs (Lei 10.214/2001) |
| Dominio: DREX | 2026-06-03 | 3 enums, 2 VOs, 1 validator, 7 testes, docs |
| Open Finance Fase 4 | 2026-06-03 | Permissoes de seguros, previdencia, investimentos, cambio + validators |
| PIX: MandatoPix (Recorrencia) | 2026-06-03 | MandatoPix VO + validator com limites (Res. BCB 191) |
| PLD/FT: validador PEP | 2026-06-03 | Cargos PEP, prazo de monitoramento 5 anos (Res. BCB 277) |
| PLD/FT: Jurisdicao alto risco GAFI | 2026-06-03 | Lista preta + cinza GAFI com detector |
| Recebiveis: AgendaRecebivel | 2026-06-03 | Datas de liquidacao, saldo projetado |
| Infra: Codecov + Checkstyle + Editorconfig | 2026-06-03 | Jacoco, checkstyle.xml, .editorconfig, script normas-coverage.sh |
| Modulo commons | 2026-06-03 | CpfCnpjValidator compartilhado, removida duplicacao |
| PIX: validador End-to-End ID | 2026-06-03 | Formato E{ISPB}{data}{hora}{sequencial}, 24-35 chars |
| PIX: validador de txid | 2026-06-03 | 26 a 35 caracteres alfanumericos (Res. BCB 1) |
| PIX: payload QR Code EMVCo | 2026-06-03 | TLV, PFI=01, GUI BR.GOV.BCB.PIX, CRC |
| CI/CD GitHub Actions | 2026-06-03 | CI (`mvn verify`) + publish GitHub Packages + Codecov |
| Publicacao GitHub Packages | 2026-06-03 | distributionManagement configurado |
| Estrutura multi-modulo Maven | 2026-05-03 | Parent BOM + 11 modulos |
| 5 dominios iniciais | 2026-05-03 | Recebiveis, PIX, Open Finance, Arranjos, PLD/FT |

---

## Bugs Conhecidos

| ID | Descricao | Severidade | Reportado em |
|----|-----------|------------|--------------|
| — | — | — | — |

---

## Notas e Decisoes

- Validators retornam `Optional<T>` em vez de lancar excecoes — permite composicao sem try/catch.
- Value objects sao records Java 21 com validacao no construtor compacto.
- Enums incluem javadoc com referencia ao artigo da norma — essencial para rastreabilidade.
- Sem Spring Boot: puro Java para maxima portabilidade e uso como biblioteca.
- Modulo `commons` centraliza validacoes compartilhadas (CPF, CNPJ) para eliminar duplicacao entre dominios.

---

## Historico de Versoes

| Versao | Data | Principais entregas |
|--------|------|---------------------|
| `1.2.0` | 2026-06-03 | 4 novos dominios (Cambio, Credito, SPB, DREX), Open Finance Fase 4, PIX Automatico, PLD/FT PEP+GAFI, AgendaRecebivel, Codecov+Checkstyle+Editorconfig |
| `1.1.0` | 2026-06-03 | Modulo commons, validadores PIX (E2E ID, txid, QR Code EMVCo), CI/CD, GitHub Packages |
| `1.0.0` | 2026-05-03 | Versao inicial com 5 dominios regulatorios |
