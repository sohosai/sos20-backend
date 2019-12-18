FROM openjdk:11-jdk AS jdk
FROM openjdk:11-jre AS jre

# Build
FROM jdk AS build
WORKDIR /src
COPY . /src
RUN ./gradlew --no-daemon shadowJar

# Runtime
FROM jre AS runtime
COPY --from=build /src/build/libs/sos-*.jar /usr/run/sos.jar
COPY --from=build /src/application-prod.conf /usr/run/application.conf

WORKDIR /usr/run

CMD java $JAVA_OPTS -jar sos.jar -config=./application.conf