FROM openjdk:17-jdk-slim
COPY target/dependency /dependency
COPY target/jmh-shardingsphere5-*.jar /dependency
COPY src/main/bin/run.sh /run.sh
ENV CLASSPATH='/dependency/*' JAVA_OPTS='-Dshardingsphere.configurationFile=/shardingsphere.yaml' JMH_ARGS='icu.wwj.jmh.shardingsphere5.ShardingSpherePointSelectBenchmark'
ENTRYPOINT /run.sh
