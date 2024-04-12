FROM openjdk:11

COPY shopping-0.0.1-SNAPSHOT.jar shopping.jar

ENTRYPOINT ["java","-jar","/shopping.jar"]