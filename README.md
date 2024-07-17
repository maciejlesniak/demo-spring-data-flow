## Spring Data Flow demo

### How to start / stop infrastructure

```shell
## to download docker compose YAML files (do it once)
make download

## to start docker-compose components
make up
```

```shell
## to tear down docker-compose components
make down
```

For more options see contents of `Makefile`.


### Management

```shell
## To run a data flow CLI
make dataflow-shell
```

```shell
## To run Skipper CLI
make skipper-shell
```


### Infrastructure components

* [Dataflow Server](https://dataflow.spring.io/docs/)
* [Spring Cloud Skipper](https://docs.spring.io/spring-cloud-skipper/docs/2.11.3/reference/htmlsingle/)
* [Kafka broker & Zookeeper](https://hub.docker.com/r/confluentinc/cp-kafka/)
* [Postgres](https://hub.docker.com/_/postgres)
* app-import-stream
* app-import-task
