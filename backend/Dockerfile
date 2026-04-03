FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Instala o bash e dos2unix pois o Alpine usa ash por padrão 
# e scripts criados no Windows podem ter problemas de final de linha (\r\n)
RUN apk add --no-cache bash dos2unix

# Copia apenas os arquivos de configuração do Gradle primeiro (otimiza cache de camadas)
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Converte o gradlew para formato Unix e dá permissão de execução
RUN dos2unix gradlew && chmod +x gradlew


COPY src src

RUN ./gradlew bootJar --no-daemon

# Estágio 2: Runtime (Apenas JRE - Muito mais leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Boa prática: Criar um usuário não-root para rodar a aplicação (Segurança)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Parâmetros de otimização da JVM para containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]