package pl.sparkidea.springcloud.demo.sourcekafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoSourceKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSourceKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoSourceKafkaApplication.class, args);
    }

    @Bean
    public Supplier<Message<String>> demoData() {
        return () -> {
            var ts = Instant.now().toEpochMilli();
            var key = ((Long) (ts % 3)).toString().getBytes();
            var payload = "Demo message; ts: %d".formatted(ts);
            LOG.info("Message produced {}", payload);

            return MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.KEY, key)
                    .setHeader("partitionKey", key)
                    .build();
        };
    }

}
