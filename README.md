# Territory Predictive Insights

## 1. Sobre o projeto

O **Territory Predictive Insights** é um sistema desenvolvido em Java com Spring Boot para monitoramento territorial e identificação preventiva de situações de risco ambiental.

A aplicação simula sensores responsáveis por coletar informações ambientais de uma região monitorada, armazena as medições em um banco de dados e utiliza estratégias de previsão para identificar possíveis condições de risco.

O sistema também realiza a geração automática de alertas classificados em três níveis de gravidade: ATENCAO, ALERTA e CRITICO.

O projeto foi desenvolvido como parte da Global Solution do segundo semestre de 2026 da disciplina de Programação Orientada a Objetos da FIAP.

## 2. Público beneficiado

O sistema é destinado a associações comunitárias, organizações locais e órgãos públicos responsáveis pelo monitoramento de regiões vulneráveis a alagamentos.

A população que vive nessas regiões é a principal beneficiada, pois está mais exposta aos impactos de chuvas intensas e elevação do nível da água.

O sistema apoia a identificação de possíveis situações de risco, ajudando os responsáveis pelo território a priorizar inspeções, verificar pontos vulneráveis e organizar ações preventivas.

A aplicação funciona como uma ferramenta de apoio à tomada de decisão, apresentando informações que podem auxiliar na identificação antecipada de problemas ambientais.

## 3. Objetivo

Desenvolver uma aplicação capaz de monitorar indicadores ambientais, armazenar seu histórico, realizar previsões e emitir alertas automáticos.

O projeto também tem como objetivo aplicar os principais conceitos de Programação Orientada a Objetos, persistência de dados, padrões criacionais e testes automatizados.

## 4. Tecnologias utilizadas

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Data JPA
- Hibernate
- H2 Database
- JUnit 5
- Mockito
- Git e GitHub

## 5. Sensores monitorados

O sistema possui três sensores simulados, responsáveis por realizar leituras ambientais.

| Sensor | Indicador | Unidade | Faixa simulada |
|---|---|---|---|
| Pluviômetro | Intensidade da chuva | mm/h | 0 a 100 |
| Sensor de nível da água | Nível da água | cm | 0 a 300 |
| Sensor de umidade do solo | Umidade do solo | % | 0 a 100 |

As leituras são geradas aleatoriamente utilizando `Math.random()`.

Os sensores verificam seu funcionamento antes de realizar uma leitura. Caso estejam indisponíveis, o sistema lança uma exceção personalizada.

Cada medição válida é registrada no banco de dados.

## 6. Funcionalidades do sistema

O sistema possui sete opções principais disponíveis pelo menu de console:

### 1. Verificar sensores

Verifica o funcionamento dos três sensores e realiza novas leituras, armazenando as medições no banco de dados.

### 2. Executar previsões

Permite selecionar um sensor e uma das estratégias de previsão disponíveis:

- Média móvel
- Comparação

O resultado apresenta o sensor analisado, a estratégia utilizada e o valor previsto.

Após o cálculo, o sistema compara a previsão com os limites configurados e gera um alerta quando necessário.

### 3. Consultar histórico

Permite consultar todas as medições armazenadas ou filtrar os registros por sensor.

O histórico é apresentado da medição mais recente para a mais antiga.

### 4. Gerenciar dados e limiares

Permite consultar e alterar os limites utilizados durante o monitoramento:

- Limite de intensidade da chuva
- Limite do nível da água
- Limite de umidade do solo

As alterações são validadas para impedir valores inválidos.

### 5. Consultar status completo

Apresenta um resumo geral do território, incluindo:

- Funcionamento dos sensores
- Limites configurados
- Últimas medições de cada sensor
- Quantidade de alertas registrados
- Último nível de alerta identificado

### 6. Consultar alertas

Permite visualizar todos os alertas armazenados, filtrar por sensor ou consultar alertas de determinado nível de gravidade.

Cada alerta apresenta o sensor relacionado, o valor previsto, o limite ultrapassado, o nível de gravidade, o motivo e a data e hora de criação.

### 7. Sair

Encerra a execução do sistema.

## 7. Estratégias de previsão

O projeto implementa duas estratégias de previsão utilizando uma classe abstrata chamada `EstrategiaPrevisao`.

### Média móvel

Calcula a média aritmética das cinco medições mais recentes de determinado sensor.

Quando existem menos de cinco medições disponíveis, utiliza todas as medições existentes, desde que haja pelo menos uma.

A fórmula utilizada é:

Média = Soma das medições / Quantidade de medições

Essa estratégia permite suavizar variações e estimar o comportamento do indicador ambiental.

### Comparação

Compara as duas medições mais recentes para identificar a tendência do indicador.

A previsão é calculada utilizando a diferença entre as duas medições e projetando essa variação para a próxima leitura.

Exemplo:

- Medição anterior: 60 cm
- Medição atual: 80 cm
- Diferença: 20 cm
- Previsão: 100 cm

Essa estratégia permite identificar tendências de aumento ou diminuição dos indicadores monitorados.

As previsões são estimativas simplificadas e não representam garantias de acontecimentos futuros.

## 8. Padrão criacional utilizado

### Factory

O projeto utiliza o padrão criacional Factory por meio da classe `FabricaPrevisao`.

A Factory centraliza a criação das estratégias de previsão, permitindo selecionar qual implementação será utilizada de acordo com o tipo informado.

As estratégias disponíveis são:

- `MEDIA_MOVEL`
- `COMPARACAO`

A escolha da Factory ocorreu porque ela permite centralizar a criação dos objetos de previsão, evitando espalhar a lógica de instanciação pelo serviço de monitoramento.

Dessa maneira, o serviço solicita a estratégia desejada sem precisar conhecer diretamente os detalhes de construção de cada implementação.

Essa organização também facilita a manutenção e permite adicionar novas estratégias futuramente.

## 9. Sistema de alertas

As previsões são comparadas com os limites configurados para cada indicador ambiental.

O sistema utiliza três níveis de alerta:

| Condição identificada | Classificação |
|---|---|
| Previsão abaixo do limite | Nenhum alerta |
| Previsão igual ou superior ao limite | ATENCAO |
| Previsão igual ou superior a 1,2 vez o limite | ALERTA |
| Previsão igual ou superior a 1,5 vez o limite | CRITICO |

A classificação verifica primeiro a condição crítica, depois a condição de alerta e, por último, a condição de atenção.

### Limites iniciais

| Indicador | Limite inicial |
|---|---:|
| Chuva | 40 mm/h |
| Nível da água | 120 cm |
| Umidade do solo | 60% |

Esses valores são ilustrativos e utilizados para a simulação acadêmica. Não representam limites oficiais de risco ambiental.

Os limites podem ser modificados durante a execução da aplicação.

Quando uma previsão atinge ou ultrapassa o limite configurado, o sistema cria um alerta contendo:

- Tipo do sensor
- Valor previsto
- Limite ultrapassado
- Nível de gravidade
- Motivo do alerta
- Data e hora de criação

Os alertas são armazenados no banco de dados e podem ser consultados posteriormente.

## 10. Persistência de dados

A aplicação utiliza Spring Data JPA e o banco de dados H2 para armazenar as informações do sistema.

As principais entidades persistidas são:

**Medicao:** armazena as leituras realizadas pelos sensores, incluindo tipo, valor, unidade e data e hora.

**Alerta:** armazena os alertas gerados, incluindo sensor, valor previsto, limite ultrapassado, nível de gravidade, motivo e data e hora.

Os dados são armazenados em um banco H2 em arquivo, localizado na pasta `data`.

Essa configuração permite preservar o histórico de medições e alertas mesmo após o encerramento da aplicação.

Durante os testes automatizados, também é disponibilizado um banco H2 em memória, separado do banco principal.

## 11. Estrutura do projeto

O projeto está organizado em cinco pacotes principais:

```text
br.com.territory/
│
├── TerritoryPredictiveInsightsApplication.java
│
├── dominio/
│   ├── ComponenteTerritorial.java
│   ├── AreaMonitorada.java
│   ├── DadosTerritorio.java
│   ├── NivelAlerta.java
│   ├── Sensor.java
│   ├── Pluviometro.java
│   ├── SensorNivelAgua.java
│   ├── SensorUmidadeSolo.java
│   ├── Medicao.java
│   └── Alerta.java
│
├── estrategia/
│   ├── EstrategiaPrevisao.java
│   ├── ResultadoPrevisao.java
│   ├── PrevisaoMediaMovel.java
│   ├── PrevisaoComparacao.java
│   ├── TipoPrevisao.java
│   └── FabricaPrevisao.java
│
├── excecoes/
│   ├── DadosInvalidosException.java
│   └── FonteIndisponivelException.java
│
├── repository/
│   ├── MedicaoRepository.java
│   └── AlertaRepository.java
│
└── servico/
    ├── ServicoMonitoramento.java
    └── MenuConsole.java
```

## 12. Tratamento de exceções

O projeto possui duas exceções personalizadas:

### DadosInvalidosException

Utilizada quando o sistema recebe informações ou valores inválidos, como limites negativos, campos obrigatórios vazios ou medições fora das faixas permitidas.

### FonteIndisponivelException

Utilizada quando um sensor não está funcionando ou não consegue fornecer uma medição.

As exceções são lançadas nos pontos de validação e tratadas nas operações correspondentes do sistema, apresentando mensagens para o usuário.

## 13. Testes automatizados

Os testes são desenvolvidos utilizando JUnit 5 e Mockito.

A aplicação possui testes organizados por responsabilidade:

- Validação dos dados territoriais
- Funcionamento dos sensores
- Tratamento de exceções personalizadas
- Cálculos de média móvel e comparação
- Criação das estratégias pela Factory
- Registro e consulta de medições
- Execução das previsões
- Geração de alertas
- Atualização dos limites

Os testes seguem o padrão AAA:

- Arrange: preparação dos dados
- Act: execução da funcionalidade
- Assert: verificação do resultado

O Mockito é utilizado para simular os repositórios, permitindo testar a lógica do serviço sem depender do banco de dados real.

### Executar os testes

No terminal, utilize:

```powershell
.\mvnw.cmd test
```

## 14. Como executar o projeto

### Pré-requisitos

- Java 21 instalado
- Maven Wrapper disponível no projeto
- IntelliJ IDEA ou outra IDE compatível com Java

### Executar a aplicação

Abra o terminal na pasta principal do projeto e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Após a inicialização do Spring Boot, o menu de interação será apresentado automaticamente no console.

### Compilar o projeto

```powershell
.\mvnw.cmd -DskipTests compile
```

### Executar todos os testes

```powershell
.\mvnw.cmd test
```

## 15. Decisões de desenvolvimento

O projeto utiliza Java com Spring Boot e Spring Data JPA para organizar as regras de negócio e a persistência dos dados.

Foi escolhido o banco H2 em arquivo para permitir que o histórico de medições e alertas permaneça disponível após o encerramento da aplicação.

A organização em cinco pacotes foi adotada para separar as responsabilidades do sistema:

- `dominio`: elementos e entidades do território
- `estrategia`: cálculos e criação das estratégias de previsão
- `excecoes`: erros personalizados
- `repository`: persistência dos dados
- `servico`: regras de negócio e interação pelo console

O padrão Factory foi utilizado na criação das estratégias de previsão, centralizando essa responsabilidade na classe `FabricaPrevisao`.

As previsões utilizam cálculos simples, média móvel e comparação entre medições, evitando algoritmos estatísticos desnecessários para a proposta acadêmica.

A aplicação utiliza um menu de console porque uma interface gráfica ou API REST não são necessárias para atender aos requisitos da atividade.

## 16. Reflexão crítica

O sistema deve evitar apresentar previsões como informações totalmente confiáveis quando não existe um histórico suficiente de medições. Embora a estratégia de média móvel consiga calcular um resultado com menos de cinco medições, esse resultado deve ser considerado preliminar. Uma melhoria futura seria estabelecer uma quantidade mínima de leituras antes de emitir alertas considerados confiáveis. Também seria importante evitar decisões baseadas em uma única leitura isolada, pois valores simulados podem apresentar variações que não representam necessariamente uma mudança real no território.

Os dados utilizados possuem limitações importantes. Um sensor representa apenas um ponto de medição e não consegue descrever todas as condições de uma região. Além disso, fatores como topografia, capacidade de drenagem, manutenção das galerias pluviais e distribuição das chuvas não são representados completamente pela simulação. Os valores aleatórios também não reproduzem necessariamente o comportamento de um evento climático real. Portanto, os resultados precisam ser interpretados considerando as limitações dos dados disponíveis.

Os alertas devem apoiar a decisão dos responsáveis pelo território, e não decidir por eles. O sistema apresenta indicadores e possíveis situações de risco, mas a avaliação humana continua necessária para considerar o contexto local, verificar as condições reais e determinar quais ações devem ser tomadas. O objetivo da aplicação é fornecer informações que auxiliem na organização de ações preventivas, sem substituir a análise dos profissionais responsáveis ou os procedimentos oficiais de segurança.

## 17. Uso de IA

Durante o desenvolvimento do Territory Predictive Insights, foi utilizada a ferramenta ChatGPT (OpenAI) como apoio.

### Onde a IA foi utilizada

- Planejamento e organização das etapas de desenvolvimento
- Explicação de conceitos de Programação Orientada a Objetos, Java e Spring Boot
- Apoio na implementação de classes, métodos e tratamento de exceções
- Sugestões de implementação das estratégias de previsão e do padrão Factory
- Apoio na elaboração dos testes automatizados com JUnit 5 e Mockito
- Organização, revisão e elaboração da documentação do projeto

### Como as sugestões foram revisadas

As sugestões fornecidas pela IA foram utilizadas como material de apoio e estudo, não sendo consideradas automaticamente corretas.

Os códigos propostos foram analisados pelo desenvolvedor, que revisou sua lógica, verificou a compatibilidade com a estrutura do projeto e comparou as implementações com os requisitos da atividade.

Também foram realizadas verificações por meio de compilação, execução da aplicação e testes automatizados para identificar possíveis problemas e realizar correções.

O desenvolvedor é responsável pela versão final do código e deve compreender o funcionamento das implementações utilizadas no projeto.

## 18. Organização do desenvolvimento

O projeto foi desenvolvido individualmente, sem divisão de tarefas entre integrantes.

O desenvolvimento, planejamento, implementação, integração, testes e documentação foram realizados pelo próprio desenvolvedor, com apoio de ferramentas de inteligência artificial para estudo e orientação.

---

**FIAP — Global Solution 2º/2026**

**Projeto:** Territory Predictive Insights

**Disciplina:** Programação Orientada a Objetos