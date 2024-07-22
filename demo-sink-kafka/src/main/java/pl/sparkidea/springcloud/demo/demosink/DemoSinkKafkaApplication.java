package pl.sparkidea.springcloud.demo.demosink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class DemoSinkKafkaApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSinkKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoSinkKafkaApplication.class, args);
    }

    @Bean
    public Consumer<String> messageLogger() {
        return LOG::info;
    }
}
