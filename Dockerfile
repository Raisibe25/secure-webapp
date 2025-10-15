FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package 
FROM tomcat:10.1-jdk17-temurin

# Install xmlstarlet for safe XML editing
RUN apt-get update && apt-get install -y xmlstarlet

# Enable GZIP compression in Tomcat connector
RUN xmlstarlet ed --inplace \
  -u "/Server/Service/Connector[@port='8080']/@compression" -v "on" \
  -u "/Server/Service/Connector[@port='8080']/@compressableMimeType" -v "text/html,text/xml,text/plain,text/css,application/json,application/javascript" \
  /usr/local/tomcat/conf/server.xml

# Copy WAR file
COPY --from=builder /app/target/secure-webapp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
