# Territory Predictive Insights

## Global Solution 2º/2026 — Programação Orientada a Objetos

**Plataforma de monitoramento territorial, análise de dados e previsão de riscos ambientais e energéticos.**

---

## 1. Sobre o projeto

O **Territory Predictive Insights** é uma aplicação Java desenvolvida para monitorar condições territoriais por meio de sensores de temperatura, umidade e consumo de energia.

O sistema permite registrar medições, classificar as condições monitoradas, gerar alertas e executar previsões utilizando diferentes estratégias de análise de dados históricos.

A proposta é transformar dados coletados em informações que auxiliem no acompanhamento de regiões e na identificação antecipada de possíveis situações de atenção ou risco.

## 2. Público-alvo

A solução é direcionada a:

- Órgãos públicos e equipes de gestão territorial;
- Equipes responsáveis pelo monitoramento ambiental;
- Profissionais de planejamento e infraestrutura;
- Organizações que precisam acompanhar indicadores ambientais e energéticos;
- Gestores que necessitam de informações para apoiar decisões preventivas.

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

O sistema implementa duas estratégias:

### Média móvel — `PrevisaoMediaMovel`

Calcula a média das últimas medições disponíveis, considerando até cinco registros. O objetivo é identificar uma tendência com base nos valores mais recentes.

### Comparação de períodos — `PrevisaoComparacao`

Compara dois períodos do histórico de medições e utiliza a variação observada para estimar um próximo valor.

As duas estratégias herdam de `EstrategiaPrevisao` e implementam seu próprio comportamento por meio do método `prever()`.

## 7. Padrão de projeto

Foi utilizado o padrão **Factory**, implementado pela classe `FabricaPrevisao`.

A fábrica centraliza a criação das estratégias de previsão, permitindo selecionar entre média móvel e comparação de períodos sem concentrar a lógica de instanciação no menu ou nos serviços.

Essa abordagem reduz o acoplamento e facilita a inclusão de novas estratégias no futuro.

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

Foram desenvolvidos testes utilizando JUnit 5 e Mockito para verificar o comportamento das classes e funcionalidades.

Os testes contemplam:

- Validação dos dados territoriais;
- Funcionamento dos sensores;
- Verificação das classificações territoriais;
- Validação de limites;
- Tratamento de exceções personalizadas;
- Funcionamento dos serviços de monitoramento;
- Operações de persistência.

A execução dos testes é realizada pelo comando:

```bash
mvn clean test
```

## 13. Equipe

- Pedro Luis Tofoli
- Raphael Talarico Nascimento Silva
- Nelson Troccoli Santos Neto

### Responsabilidade pelo desenvolvimento

Pedro Luis Tofoli foi responsável pelo desenvolvimento técnico integral da solução, incluindo a estruturação do projeto, implementação das classes e funcionalidades, integração com o banco de dados, estratégias de previsão, tratamento de exceções, testes automatizados e documentação.

## 14. Reflexão crítica

O Territory Predictive Insights foi desenvolvido com o objetivo de demonstrar como a programação orientada a objetos, a persistência de dados e estratégias simples de previsão podem contribuir para o monitoramento territorial.

Entretanto, é importante reconhecer que previsões e alertas não devem ser tratados como verdades absolutas. O sistema utiliza leituras simuladas, e seus resultados dependem diretamente da qualidade, quantidade e representatividade dos dados disponíveis. Em uma aplicação real, seria necessário integrar sensores físicos confiáveis, realizar calibrações periódicas e considerar fatores externos que podem influenciar as condições de uma região.

O sistema também deve permanecer silencioso quando não houver dados suficientes, quando as fontes estiverem indisponíveis ou quando as medições não apresentarem confiabilidade. Gerar alertas sem evidências adequadas poderia provocar alarmes desnecessários e comprometer a credibilidade da solução.

Outro ponto importante é que os níveis NORMAL, ATENÇÃO, ALERTA e CRÍTICO representam classificações baseadas em limites configurados. Eles servem como instrumentos de apoio ao acompanhamento e à tomada de decisão, mas não substituem a análise de profissionais responsáveis. Uma situação crítica deve ser investigada por pessoas capacitadas antes da definição de qualquer intervenção.

As previsões implementadas também possuem limitações: a média móvel considera os valores recentes, enquanto a comparação de períodos utiliza a variação observada no histórico. Nenhuma das estratégias considera, nesta versão, sazonalidade, condições climáticas externas ou outros fatores complexos. Portanto, seus resultados devem ser interpretados como estimativas.

Por fim, ferramentas de inteligência artificial podem auxiliar na organização de ideias, revisão de código e compreensão de conceitos, mas a validação das funcionalidades, a análise dos resultados e a responsabilidade pelo funcionamento da aplicação devem permanecer com os desenvolvedores. A evolução do projeto deve priorizar a confiabilidade dos dados, a transparência dos alertas e a utilização responsável das previsões.

## 15. Considerações finais

O projeto demonstra a aplicação prática dos principais conceitos de Programação Orientada a Objetos, incluindo abstração, encapsulamento, herança, polimorfismo, interfaces, tratamento de exceções, padrões de projeto, testes automatizados e persistência de dados.

A solução pode ser expandida futuramente com integração a sensores reais, fontes públicas de dados ambientais, exportação de relatórios e estratégias de previsão mais avançadas.

---

**Repositório:** https://github.com/Pedro184294/territory-predictive-insights