# === .dockerignore CONSIGLIATO ===
# Assicurati che .m2/settings.xml NON sia ignorato. Puoi ignorare il resto del contenuto di .m2
# .m2/repository/
# target/
# .git/
# ...

# ====================================================================
# FASE 1: BUILD (Compilazione)
# ====================================================================
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /

COPY pom.xml .
COPY .m2 /root/.m2 


COPY .env ./.env

# 3. Copia il codice sorgente
COPY src /src



# **Metodo Standard e più affidabile:**

#RUN mvn clean install -DskipTests

# Se l'istruzione precedente fallisce, prova a specificare esplicitamente il percorso del settings.xml:
RUN export $(cat .env | xargs) && mvn -s /root/.m2/settings.xml clean install -DskipTests


# ====================================================================
# FASE 2: RUNTIME (Esecuzione - Nessuna modifica)
# ====================================================================
FROM tomcat:10.1-jre17-temurin-jammy

RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copia l'artefatto WAR compilato
COPY --from=builder /target/emailModule-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copia il file .env per il runtime
COPY --from=builder /.env /usr/local/tomcat/.env

EXPOSE 8082