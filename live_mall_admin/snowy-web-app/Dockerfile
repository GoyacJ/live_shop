# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjre-debian:17-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.3-cds
#FROM findepi/graalvm:java17-native

LABEL maintainer="GMQ"

ARG FILE_NAME=target/*.jar

WORKDIR app

ADD $FILE_NAME app.jar

ENV SERVER_PORT=8080 LANG=C.UTF-8 LC_ALL=C.UTF-8 SERVER_ENV=dev JAVA_OPTS="-Xmx1024m -Xms512m" TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8 -Dserver.port=${SERVER_PORT} -Dsolon.env=${SERVER_ENV} \
           # 应用名称 如果想区分集群节点监控 改成不同的名称即可
           #-Dskywalking.agent.service_name=zhg-server \
           #-javaagent:/zhg/skywalking/agent/skywalking-agent.jar \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
           -jar app.jar

