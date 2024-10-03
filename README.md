# queue-simulator
Simulador genérico de filas desenvolvido para a disciplina de Simulação e Métodos Analíticos da PUCRS no semestre 2024/2

Grupo: Diogo Pessin Camargo, Fernanda Ferreira de Mello, Gustavo Beche Lopes, William de Lima Schneider

### Pré-requisitos para execução do simulador
- Ter Java 17 instalado em sua máquina

### Como executar o simulador?
1. Essa aplicação utiliza a ferramenta Gradle para automatização do build do projeto. Para baixar a versão adequada do Gradle em sua máquina, execute o comando abaixo:
```shell
gradlew.bat wrapper #Em Windows
./gradlew wrapper #Em Linux
```
2. Na pasta src/main/resources/ do projeto, existe um arquivo `queue-config.yaml` onde podem ser configuradas todas as propriedades do simulador. As propriedades deste arquivo que são necessárias para a execução do trabalho estão descritas abaixo:
    - **queues:** enumera as filas utilizadas pelo simulador e suas características principais (capacidade, número de servidores, e intervalos de chegada e de atendimento de clientes em minutos). O nome de cada uma destas filas deve iniciar com Q, seguido por uma numeração (por exemplo: Q2)
    - **network:** enumera as possibilidades de roteamento entre filas, onde cada elemento apresenta um source (fila de origem), um target (fila de destino ou saída do sistema), e uma probabilidade de ocorrer este roteamento de 0 a 1
    - **rndnumbersPerSeed:** indica a quantidade de números aleatórios a serem utilizados pela aplicação
    - **seeds:** indica a lista de sementes utilizadas para gerar números pseudo-aleatórios. Neste simulador, apenas a primeira semente da lista é utilizada.
    - **arrivals:** indica o momento das primeiras chegadas em cada fila do simulador, em minutos
2. Executar a aplicação a partir do Gradle, ou gerar um jar executável e executá-lo via linha de comando
```shell
# Para executar via Gradle
gradlew.bat clean build run #Em Windows
./gradlew clean build run #Em Linux

# Para executar jar via linha de comando
gradlew.bat clean build jar #Em Windows
./gradlew clean build jar #Em Linux
java -jar /build/libs/queue-simulator-1.0-SNAPSHOT.jar
```