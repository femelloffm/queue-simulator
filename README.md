# queue-simulator
Simulador de uma fila simples desenvolvido para a disciplina de Simulação e Métodos Analíticos da PUCRS no semestre 2024/2

Grupo: Diogo Pessin Camargo, Fernanda Ferreira de Mello, Gustavo Beche Lopes, William de Lima Schneider

### Pré-requisitos para execução do simulador
- Ter Java 17 instalado em sua máquina

### Como executar o simulador?
1. Essa aplicação utiliza a ferramenta Gradle para automatização do build do projeto. Para baixar a versão adequada do Gradle em sua máquina, execute o comando abaixo:
```shell
gradlew.bat wrappper #Em Windows
./gradlew wrapper #Em Linux
```
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