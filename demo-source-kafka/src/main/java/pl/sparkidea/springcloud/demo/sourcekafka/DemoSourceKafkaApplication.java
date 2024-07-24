package pl.sparkidea.springcloud.demo.sourcekafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoSourceKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSourceKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoSourceKafkaApplication.class, args);
    }

    @Bean
    public Supplier<String> demoData() {
        return () -> {
            var message = "Demo message; ts: %d".formatted(Instant.now().toEpochMilli());
            LOG.info(message);
            return message;
        };
    }

}
