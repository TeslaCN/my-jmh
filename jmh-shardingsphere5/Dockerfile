FROM openjdk:17-jdk-slim
COPY target/dependency /dependency
COPY target/jmh-shardingsphere5-*.jar /benchmark.jar
COPY src/main/bin/run.sh /run.sh
ENV CLASSPATH='/dependency/*:/benchmark.jar' JAVA_OPTS='-Dshardingsphere.configurationFile=/shardingsphere.yaml' JMH_ARGS='icu.wwj.jmh.shardingsphere5.*'
ENTRYPOINT /run.sh
