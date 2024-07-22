#!/bin/sh

export DATAFLOW_VERSION=2.11.3
export SKIPPER_VERSION=2.11.3

## WARNING: Spring Cloud Data Flow defaults to Java 8 when running applications
export BP_JVM_VERSION=-jdk17
export HOST_MOUNT_PATH=.data/scdf

DOWNLOAD_URL_BASE		:=https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose
DATA_FLOW_CLI_URL		:=https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-shell/$(DATAFLOW_VERSION)/spring-cloud-dataflow-shell-$(DATAFLOW_VERSION).jar
SKIPPER_CLI_URL			:=https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-skipper-shell/$(SKIPPER_VERSION)/spring-cloud-skipper-shell-$(SKIPPER_VERSION).jar
DATA_FLOW_DASHBOARD_URL	:=http://localhost:9393/dashboard
SKIPPER_API_URL			:=http://localhost:7577/api
CF_CENTER_DASHBOARD_URL	:=http://localhost:9021/
BROKER					:=kafka
DATABASE				:=postgres

download:
	wget -O compose-scdf.yml 					$(DOWNLOAD_URL_BASE)/docker-compose.yml;
	wget -O compose-"$(DATABASE)".yml 			$(DOWNLOAD_URL_BASE)/docker-compose-"${DATABASE}".yml;
#	wget -O compose-"$(BROKER)".yml 			$(DOWNLOAD_URL_BASE)/docker-compose-"${BROKER}".yml;
	wget -O compose-kafka.yml 					https://raw.githubusercontent.com/confluentinc/cp-all-in-one/7.6.2-post/cp-all-in-one-kraft/docker-compose.yml
	wget -O spring-cloud-dataflow-shell.jar 	$(DATA_FLOW_CLI_URL)
	wget -O spring-cloud-skipper-shell.jar 		$(SKIPPER_CLI_URL)

compose-up-kafka:
	docker-compose -f compose-$(BROKER).yml \
  		up -d broker schema-registry control-center ksqldb-server ksqldb-cli rest-proxy

compose-up-db:
	docker-compose -f compose-$(DATABASE).yml \
		up -d

compose-up:
	docker-compose \
    	-f compose-$(BROKER).yml \
    	-f compose-$(DATABASE).yml \
    	-f compose-scdf.yml \
    	-f compose-overrides.yml \
    	up -d \
    	broker schema-registry control-center ksqldb-server ksqldb-cli rest-proxy \
    	postgres \
    	dataflow-server skipper-server

compose-import:
	docker-compose \
        -f docker-compose.yml \
        up -d app-import-stream app-import-task

compose-down:
	docker-compose \
    	-f compose-$(BROKER).yml \
    	-f compose-$(DATABASE).yml \
    	-f compose-scdf.yml \
    	-f compose-overrides.yml \
    	down

ui-scdf:
	@while ! curl $(DATA_FLOW_DASHBOARD_URL) </dev/null; do sleep 3; printf "Waiting..."; done
	open $(DATA_FLOW_DASHBOARD_URL)

ui-cf-center:
	@while ! curl $(CF_CENTER_DASHBOARD_URL) </dev/null; do sleep 3; printf "Waiting..."; done
	open $(CF_CENTER_DASHBOARD_URL)

up: compose-up ui-scdf ui-cf-center

down: compose-down

clean-compose:
	rm \
		docker-compose.yml \
		docker-compose-$(BROKER).yml \
		docker-compose-$(DATABASE).yml

docker-rm:
	docker stop $$(docker ps -a -q)
	docker rm $$(docker ps -a -q)

dataflow-shell:
	@while ! curl $(DATA_FLOW_DASHBOARD_URL) </dev/null; do sleep 3; done
	java -jar spring-cloud-dataflow-shell.jar

skipper-shell:
	@while ! curl $(SKIPPER_API_URL) </dev/null; do sleep 3; done
	java -jar spring-cloud-skipper-shell.jar

mvn-package-demo:
	cd demo-processor-kafka; mvn clean package; cp target/demo-processor-kafka-0.0.1-SNAPSHOT.jar ../.data/scdf/
	cd demo-publisher-kafka; mvn clean package; cp target/demo-publisher-kafka-0.0.1-SNAPSHOT.jar ../.data/scdf/
	cd demo-sink-kafka; mvn clean package; cp target/demo-sink-kafka-0.0.1-SNAPSHOT.jar ../.data/scdf/
