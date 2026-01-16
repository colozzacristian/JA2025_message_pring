# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamo il settings.xml per l'autenticazione GitHub
COPY .m2 /root/.m2 

COPY pom.xml .
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml

COPY src ./src
RUN mvn clean package -DskipTests -s /root/.m2/settings.xml

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# --- VARIABILI D'AMBIENTE PRECONFIGURATE ---
# Qui inserisci i valori di default per il tuo modulo email
ENV GMAIL_PORT=465
ENV GMAIL_ACCOUNT=testprojectwork2025@gmail.com
ENV GMAIL_APP_PASSWORD=insertPassword
ENV whitelist_ips=127.0.0.1

COPY --from=build /app/target/emailModule-0.0.1-SNAPSHOT.jar app.jar

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

COPY .env .env

EXPOSE 8071

ENTRYPOINT ["./entrypoint.sh"]