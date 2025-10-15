FROM tomcat:10.1-jdk17-temurin
# Enable basic GZIP compression in Tomcat connector
RUN sed -i 's/<Connector port="8080"/<Connector port="8080" compression="on" compressableMimeType="text\\/html,text\\/xml,text\\/plain,text\\/css,application\\/json,application\\/javascript"/' /usr/local/tomcat/conf/server.xml
COPY target/secure-webapp.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]