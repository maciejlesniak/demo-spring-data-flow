package pl.sparkidea.springcloud.demo.demoprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class DemoProcessorKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoProcessorKafkaApplication.class, args);
    }

    @Bean
    public Function<String, String> toUpperCase() {
        return String::toUpperCase;
    }
}
