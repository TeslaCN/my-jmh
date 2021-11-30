IMAGE ?='database-jmh:latest'

install:
	./mvnw clean install -T1C -B

docker: install
	docker build -t $(IMAGE) jmh-shardingsphere5

.PHONY: install docker
