# Territory Predictive Insights

## Global Solution 2º/2026 — Programação Orientada a Objetos

**Plataforma de monitoramento territorial, análise de dados e previsão de riscos ambientais e energéticos.**

---

## 1. Sobre o projeto

O **Territory Predictive Insights** é uma aplicação Java desenvolvida para monitorar condições territoriais por meio de sensores de temperatura, umidade e consumo de energia.

O sistema permite registrar medições, classificar as condições monitoradas, gerar alertas e executar previsões utilizando diferentes estratégias de análise de dados históricos.

A proposta é transformar dados coletados em informações que auxiliem no acompanhamento de regiões, na identificação antecipada de possíveis situações de atenção ou risco e no apoio à tomada de decisões preventivas.

## 2. Público-alvo e decisões apoiadas

A solução é direcionada a:

- Órgãos públicos e equipes de gestão territorial;
- Equipes responsáveis pelo monitoramento ambiental;
- Profissionais de planejamento e infraestrutura;
- Organizações que precisam acompanhar indicadores ambientais e energéticos;
- Gestores responsáveis pela análise de condições territoriais.

O sistema busca apoiar decisões como:

- Identificar regiões que necessitam de maior acompanhamento;
- Priorizar inspeções em locais classificados como ALERTA ou CRÍTICO;
- Identificar aumentos relevantes de temperatura e consumo de energia;
- Direcionar equipes técnicas para regiões que exigem avaliação;
- Acompanhar tendências por meio do histórico de medições e das previsões;
- Avaliar a necessidade de revisão dos limites de monitoramento.

As informações geradas servem como apoio à análise dos responsáveis, não substituindo avaliações técnicas ou decisões humanas.

## 3. Funcionalidades

O sistema disponibiliza as seguintes funcionalidades:

- **Monitoramento manual:** permite informar manualmente as medições de uma região.
- **Simulação automática:** gera leituras simuladas utilizando a classe `Random`.
- **Sensores:** trabalha com sensores de temperatura, umidade e consumo de energia.
- **Validação de dados:** verifica se as medições estão dentro dos parâmetros permitidos.
- **Classificação territorial:** identifica os estados NORMAL, ATENÇÃO, ALERTA e CRÍTICO, além da condição INATIVO.
- **Sistema de alertas:** registra alertas quando são identificadas condições que exigem atenção.
- **Previsões:** executa duas estratégias de previsão utilizando o histórico de medições.
- **Histórico de medições:** permite consultar os registros armazenados.
- **Histórico por sensor:** permite consultar os dados de cada tipo de sensor.
- **Histórico de alertas:** apresenta os alertas registrados para uma região.
- **Gerenciamento de limites:** permite consultar e alterar os limites de monitoramento mediante código de acesso.

## 4. Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java 25 | Linguagem principal |
| Maven | Gerenciamento de dependências e execução dos testes |
| JDBC | Comunicação com o banco de dados |
| H2 Database | Armazenamento das medições e dos alertas |
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências nos testes |
| Git e GitHub | Versionamento e hospedagem do código |

## 5. Arquitetura e organização do projeto

O projeto foi organizado em pacotes para separar as responsabilidades e facilitar a manutenção.

```text
src/
├── main/
│   └── java/
│       └── br/com/territory/
│           ├── app/
│           │   └── Main.java
│           ├── dominio/
│           │   ├── ComponenteTerritorial.java
│           │   ├── DadosTerritorio.java
│           │   ├── RegiaoMonitorada.java
│           │   ├── Sensor.java
│           │   ├── SensorTemperatura.java
│           │   ├── SensorUmidade.java
│           │   └── SensorConsumoEnergia.java
│           ├── exceptions/
│           │   ├── DadosInvalidosException.java
│           │   └── FonteIndisponivelException.java
│           ├── persistencia/
│           │   ├── AlertaDAO.java
│           │   ├── BancoDados.java
│           │   ├── ConexaoBanco.java
│           │   └── MedicaoDAO.java
│           ├── servicos/
│           │   ├── EstrategiaPrevisao.java
│           │   ├── FabricaPrevisao.java
│           │   ├── MonitoramentoService.java
│           │   ├── PrevisaoComparacao.java
│           │   └── PrevisaoMediaMovel.java
│           └── validation/
│               └── ValidadorDados.java
└── test/
    └── java/
        └── br/com/territory/
```

### Principais conceitos de Programação Orientada a Objetos

- **Abstração:** utilização de classes abstratas para representar componentes territoriais e estratégias de previsão.
- **Encapsulamento:** atributos privados e métodos responsáveis pelo acesso e alteração controlada dos dados.
- **Herança:** utilização de classes especializadas que herdam características de classes abstratas.
- **Polimorfismo:** implementação de diferentes estratégias de previsão por meio da sobrescrita do método `prever()`.
- **Interfaces:** definição de contratos para os sensores, permitindo diferentes implementações.

## 6. Estratégias de previsão

O sistema implementa duas estratégias de previsão:

### Média móvel — `PrevisaoMediaMovel`

Calcula a média das últimas medições disponíveis, considerando até cinco registros. O objetivo é identificar uma tendência com base nos valores mais recentes.

A estratégia utiliza os registros existentes no histórico. Quando há menos de cinco medições, calcula a média utilizando os valores disponíveis. Sem medições, não é possível gerar uma previsão.

### Comparação de períodos — `PrevisaoComparacao`

Compara dois períodos do histórico de medições e utiliza a variação observada para estimar um próximo valor.

Essa estratégia exige pelo menos quatro medições para realizar a comparação. Quando a quantidade de registros é insuficiente, o sistema informa que não é possível executar a previsão.

As duas estratégias herdam de `EstrategiaPrevisao` e implementam seu próprio comportamento por meio do método `prever()`.

## 7. Padrão de projeto

Foi utilizado o padrão **Factory**, implementado pela classe `FabricaPrevisao`.

A fábrica centraliza a criação das estratégias de previsão, permitindo selecionar entre média móvel e comparação de períodos sem concentrar a lógica de instanciação no menu ou nos serviços.

Essa abordagem reduz o acoplamento, organiza a criação dos objetos e facilita a inclusão de novas estratégias no futuro.

## 8. Banco de dados e persistência

A aplicação utiliza o banco de dados H2 com acesso por JDBC.

As medições e os alertas são armazenados em tabelas próprias, permitindo consultar os dados mesmo após encerrar e iniciar novamente a aplicação.

A implementação utiliza:

- `PreparedStatement` para executar comandos SQL com parâmetros;
- `try-with-resources` para gerenciamento de conexões e recursos;
- Classes DAO para separar a persistência das regras de negócio;
- Transações para garantir a consistência dos registros relacionados.

O banco de dados local é criado no diretório `data/`, que não deve ser enviado ao repositório Git.

## 9. Tratamento de exceções e validações

Foram implementadas exceções personalizadas:

- `DadosInvalidosException`: utilizada quando uma informação não atende às regras de validação.
- `FonteIndisponivelException`: utilizada quando uma fonte de dados ou sensor não está disponível.

As validações verificam, entre outros pontos:

- Valores numéricos finitos;
- Temperaturas válidas;
- Consumo de energia não negativo;
- Umidade entre 0% e 100%;
- Limites de monitoramento válidos;
- Disponibilidade dos sensores.

Essas validações ajudam a impedir que dados inválidos sejam utilizados nos cálculos, nas classificações e nas previsões.

## 10. Como executar o projeto

### Pré-requisitos

- JDK 25;
- Maven 3.9 ou superior;
- Git.

### Clonar o repositório

```bash
git clone https://github.com/Pedro184294/territory-predictive-insights.git
```

### Entrar na pasta

```bash
cd territory-predictive-insights
```

### Executar os testes

```bash
mvn clean test
```

### Executar a aplicação

```bash
mvn exec:java "-Dexec.mainClass=br.com.territory.app.Main"
```

Na primeira execução, o sistema inicializa o banco de dados H2 e cria as tabelas necessárias.

## 11. Menu principal

A aplicação disponibiliza as seguintes opções:

| Opção | Funcionalidade |
|---|---|
| 1 | Registrar monitoramento manualmente |
| 2 | Consultar histórico de medições |
| 3 | Consultar histórico por sensor |
| 4 | Consultar histórico de alertas |
| 5 | Consultar limites de monitoramento |
| 6 | Alterar limites de monitoramento |
| 7 | Executar previsão |
| 8 | Sair |
| 9 | Simular monitoramento automaticamente |

## 12. Testes automatizados

Foram desenvolvidos testes automatizados utilizando JUnit 5 e Mockito para verificar o comportamento das classes e funcionalidades da aplicação.

Os testes contemplam:

- Validação dos dados territoriais;
- Funcionamento dos sensores;
- Verificação das classificações territoriais;
- Validação de limites;
- Tratamento de exceções personalizadas;
- Funcionamento dos serviços de monitoramento;
- Operações de persistência;
- Registro de medições e alertas;
- Verificação das descrições e dos limites associados aos alertas.

### Técnicas utilizadas

- **Padrão AAA (Arrange, Act, Assert):** organização dos testes em preparação, execução e verificação dos resultados.
- **assertThrows:** verificação do lançamento de exceções personalizadas, como `DadosInvalidosException` e `FonteIndisponivelException`.
- **Mockito:** simulação de objetos DAO para testar os serviços sem depender diretamente do banco de dados.
- **ArgumentCaptor:** captura de argumentos utilizados nas chamadas dos DAOs para verificar as informações enviadas.

### Resultado dos testes

Na última execução de `mvn clean test`, foram obtidos os seguintes resultados:

| Classe de teste | Testes executados |
|---|---:|
| DadosTerritorioTest | 8 |
| MonitoramentoServiceTest | 7 |
| PersistenciaTest | 3 |
| RegiaoMonitoradaTest | 11 |
| SensoresTest | 6 |
| ValidadorDadosTest | 7 |
| **Total** | **42** |

**Resultado:** 42 testes executados, 0 falhas, 0 erros e 0 testes ignorados.

Para executar novamente todos os testes:

```bash
mvn clean test
```

## 13. Equipe

**Integrantes do grupo:**

- Pedro Luis Tofoli
- Raphael Talarico Nascimento Silva
- Nelson Troccoli Santos Neto

### Responsabilidade pelo desenvolvimento

O desenvolvimento técnico integral do projeto foi realizado individualmente por Pedro Luis Tofoli, responsável pela estruturação da aplicação, implementação das classes e funcionalidades, integração com o banco de dados, desenvolvimento das estratégias de previsão, tratamento de exceções, testes automatizados e documentação.

Raphael Talarico Nascimento Silva e Nelson Troccoli Santos Neto permanecem como integrantes do grupo, conforme a composição definida para o trabalho.

## 14. Reflexão crítica

O Territory Predictive Insights foi desenvolvido com o objetivo de demonstrar como a programação orientada a objetos, a persistência de dados e estratégias simples de previsão podem contribuir para o monitoramento territorial.

Entretanto, é importante reconhecer que previsões e alertas não devem ser tratados como verdades absolutas. O sistema utiliza leituras simuladas, e seus resultados dependem diretamente da qualidade, quantidade e representatividade dos dados disponíveis. Em uma aplicação real, seria necessário integrar sensores físicos confiáveis, realizar calibrações periódicas e considerar fatores externos que podem influenciar as condições de uma região.

A quantidade de dados também influencia a confiabilidade das previsões. A média móvel consegue trabalhar com os registros disponíveis, mesmo quando existem menos de cinco medições, enquanto a comparação de períodos exige pelo menos quatro registros. Por isso, previsões realizadas com históricos pequenos devem ser interpretadas com cautela.

Quando não existem medições disponíveis ou quando uma fonte de dados está indisponível, o sistema não deve apresentar resultados como se fossem confiáveis. Da mesma forma, dados inválidos precisam ser tratados antes de serem utilizados nos cálculos. Gerar alertas ou previsões sem evidências adequadas poderia provocar interpretações equivocadas e comprometer a credibilidade da solução.

Outro ponto importante é que os níveis NORMAL, ATENÇÃO, ALERTA e CRÍTICO representam classificações baseadas em limites configurados. Eles servem como instrumentos de apoio ao acompanhamento e à tomada de decisão, mas não substituem a análise de profissionais responsáveis. Uma situação crítica deve ser investigada por pessoas capacitadas antes da definição de qualquer intervenção.

As previsões implementadas também possuem limitações: a média móvel considera os valores recentes, enquanto a comparação de períodos utiliza a variação observada no histórico. Nenhuma das estratégias considera, nesta versão, sazonalidade, condições climáticas externas ou outros fatores complexos. Portanto, seus resultados devem ser interpretados como estimativas, e não como garantias de acontecimentos futuros.

Por fim, ferramentas de inteligência artificial podem auxiliar na organização de ideias, revisão de código e compreensão de conceitos, mas a validação das funcionalidades, a análise dos resultados e a responsabilidade pelo funcionamento da aplicação devem permanecer com os desenvolvedores. A evolução do projeto deve priorizar a confiabilidade dos dados, a transparência dos alertas e a utilização responsável das previsões.

## 15. Uso de Inteligência Artificial

Ferramentas de inteligência artificial foram utilizadas como apoio durante o desenvolvimento do projeto, principalmente para:

- Auxiliar na compreensão de conceitos de programação orientada a objetos;
- Apoiar a análise e a revisão de trechos de código;
- Identificar possíveis problemas de implementação;
- Auxiliar na elaboração e revisão de testes automatizados;
- Apoiar a organização e a documentação do projeto;
- Esclarecer dúvidas sobre Maven, JDBC, persistência e tratamento de exceções.

As sugestões geradas por essas ferramentas foram analisadas e verificadas durante o desenvolvimento. As funcionalidades foram executadas e os testes automatizados foram utilizados para validar o comportamento da aplicação.

A inteligência artificial foi utilizada como ferramenta de apoio, não substituindo a responsabilidade pelo desenvolvimento, pela compreensão do código e pela validação dos resultados.

## 16. Considerações finais

O projeto demonstra a aplicação prática dos principais conceitos de Programação Orientada a Objetos, incluindo abstração, encapsulamento, herança, polimorfismo, interfaces, tratamento de exceções, padrões de projeto, testes automatizados e persistência de dados.

A solução permite acompanhar condições territoriais, armazenar medições, identificar situações que exigem atenção e estimar tendências a partir de dados históricos.

O sistema também pode ser expandido futuramente com integração a sensores reais, fontes públicas de dados ambientais, exportação de relatórios e estratégias de previsão mais avançadas.

---

**Repositório GitHub:** https://github.com/Pedro184294/territory-predictive-insights