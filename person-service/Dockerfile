# FROM openjdk:11-jre-slim
# FROM amazoncorretto:11
FROM kprasad99/openjdk:11-jre-alpine

ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENV JAVA_TOOLS_OPTS=$JAVA_TOOLS_OPTS

VOLUME /tmp

ENTRYPOINT ["java","-cp","app:app/lib/*","io.github.kprasad99.rsocket.PersonServiceApplication"]