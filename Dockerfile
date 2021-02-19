FROM openjdk:8
ADD target/atm-spring-boot.jar /usr/local/atm/atm-spring-boot.jar
EXPOSE 8085
WORKDIR /usr/local/atm
ENTRYPOINT ["java", "-jar", "atm-spring-boot.jar"]