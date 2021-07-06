FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY backend/target/*.jar .

RUN mkdir -p dependency && (cd dependency; jar -xf ../*.jar)

FROM metersphere/fabric8-java-alpine-openjdk8-jre

LABEL maintainer="FIT2CLOUD <support@fit2cloud.com>"

ARG MS_VERSION=dev
ARG DEPENDENCY=/workspace/app/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

RUN mv /app/jmeter /opt/
RUN mkdir -p /opt/jmeter/lib/junit

ENV JAVA_CLASSPATH=/app:/app/lib/*
ENV JAVA_MAIN_CLASS=io.metersphere.Application
ENV AB_OFF=true
ENV MS_VERSION=${MS_VERSION}
ENV JAVA_OPTIONS="-Dfile.encoding=utf-8 -Djava.awt.headless=true -javaagent:/app/cat/polaris-agent.jar -Djava._appid_=int-website-arch-autotest-metersphere -Djava._environment_=work -server -Xms8g -Xmx8g -verbose:gc -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -XX:MaxMetaspaceSize=1g -XX:PermSize=512M -XX:MaxPermSize=1g"

CMD ["/deployments/run-java.sh"]
