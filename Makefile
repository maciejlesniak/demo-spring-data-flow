#!/bin/sh

export DATAFLOW_VERSION=2.11.3
export SKIPPER_VERSION=2.11.3

## WARNING: Spring Cloud Data Flow defaults to Java 8 when running applications
export BP_JVM_VERSION=-jdk17

DOWNLOAD_URL_BASE		:=https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/main/src/docker-compose
DATA_FLOW_CLI_URL		:=https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dataflow-shell/$(DATAFLOW_VERSION)/spring-cloud-dataflow-shell-$(DATAFLOW_VERSION).jar
SKIPPER_CLI_URL			:=https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-skipper-shell/$(SKIPPER_VERSION)/spring-cloud-skipper-shell-$(SKIPPER_VERSION).jar
DATA_FLOW_DASHBOARD_URL	:=http://localhost:9393/dashboard
SKIPPER_API_URL			:=http://localhost:7577/api
BROKER					:=kafka
DATABASE				:=postgres

download:
	wget -O docker-compose.yml 					$(DOWNLOAD_URL_BASE)/docker-compose.yml;
	wget -O docker-compose-"$(BROKER)".yml 		$(DOWNLOAD_URL_BASE)/docker-compose-"${BROKER}".yml;
	wget -O docker-compose-"$(DATABASE)".yml 	$(DOWNLOAD_URL_BASE)/docker-compose-"${DATABASE}".yml;
	wget -O spring-cloud-dataflow-shell.jar 	$(DATA_FLOW_CLI_URL)
	wget -O spring-cloud-skipper-shell.jar 		$(SKIPPER_CLI_URL)

compose-up:
	docker-compose \
    	-f docker-compose.yml \
    	-f docker-compose-"$(BROKER)".yml \
    	-f docker-compose-"$(DATABASE)".yml \
    	up -d

compose-down:
	docker-compose \
    	-f docker-compose.yml \
    	-f docker-compose-"$(BROKER)".yml \
    	-f docker-compose-"$(DATABASE)".yml \
    	down

ui:
	@while ! curl $(DATA_FLOW_DASHBOARD_URL) </dev/null; do sleep 3; printf "Waiting..."; done
	open $(DATA_FLOW_DASHBOARD_URL)

up: compose-up ui

down: compose-down

clean-compose:
	rm \
		docker-compose.yml \
		docker-compose-"$(BROKER)".yml \
		docker-compose-"$(DATABASE)".yml

docker-rm:
	docker stop $$(docker ps -a -q)
	docker rm $$(docker ps -a -q)

dataflow-shell:
	@while ! curl $(DATA_FLOW_DASHBOARD_URL) </dev/null; do sleep 3; done
	java -jar spring-cloud-dataflow-shell.jar

skipper-shell:
	@while ! curl $(SKIPPER_API_URL) </dev/null; do sleep 3; done
	java -jar spring-cloud-skipper-shell.jar

