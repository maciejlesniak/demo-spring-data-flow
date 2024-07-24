package pl.sparkidea.springcloud.demo.demoprocessor;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

@SpringBootApplication
public class DemoProcessorKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoProcessorKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoProcessorKafkaApplication.class, args);
    }

    @Bean
    public Function<Message<String>, Message<String>> toUpperCase() {
        return inputMessage -> {
            LOG.info("[1. upper case] Got input message {}", inputMessage);

            return new StringMessage(
                    inputMessage.getPayload().toUpperCase(Locale.ROOT),
                    Map.of(
                            "originalMessageString", inputMessage.getPayload(),
                            "processingTs", Instant.now().toEpochMilli()
                    )
            );
        };
    }

    @Bean
    public Function<Message<String>, Message<String>> addStaticString() {
        return inputMessage -> {
            LOG.info("[2. add static string] Got input message {}", inputMessage);

            return new StringMessage(
                    "%s -- additional string in lower case".formatted(inputMessage.getPayload()),
                    Map.of(
                            "originalMessageString", inputMessage.getPayload(),
                            "processingTs", Instant.now().toEpochMilli()
                    )
            );
        };
    }

    private record StringMessage(String payload, Map<String, Object> headers) implements Message<String> {

        @NotNull
        @Override
        public String getPayload() {
            return this.payload;
        }

        @NotNull
        @Override
        public MessageHeaders getHeaders() {
            return new MessageHeaders(headers);
        }
    }

}
