package pl.sparkidea.springcloud.demo.publisherkafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.util.function.Supplier;

@SpringBootApplication
public class DemoPublisherKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoPublisherKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoPublisherKafkaApplication.class, args);
    }

    @Bean
    public Supplier<String> demoData() {
        return () -> {
            var message = "Demo message; ts: %d".formatted(Instant.now().toEpochMilli());
            LOG.info(message);
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return message;
        };
    }

}
