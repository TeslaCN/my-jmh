
install:
	./mvnw clean install -T1C -B

docker: install
	docker build -t database-jmh:latest jmh-shardingsphere5

.PHONY: install docker
