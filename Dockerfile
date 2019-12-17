FROM openjdk:11-jdk as jdk
FROM openjdk:11-jre as jre


# Build
FROM jdk as build
WORKDIR /src
COPY . /src
RUN ./gradlew --no-daemon shadowJar

# Runtime
FROM jre as runtime
RUN addgroup --gid 1000 sos && \
    adduser --uid 1000 --gid 1000 --disabled-password sos

COPY --from=build /src/build/libs/sos-*.jar /usr/run/sos.jar

USER sos
WORKDIR /usr/run

CMD java $JAVA_OPTS -jar sos.jar