package pl.sparkidea.springcloud.demo.sourcekafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EnableTestBinder
class DemoSourceKafkaApplicationTests {

    @Autowired
    private OutputDestination output;

    @Test
    void sourceFunction_shouldProduceDemoMessage() {

        // when
        var receivedMessageBytes = output.receive().getPayload();

        // then
        assertTrue(new String(receivedMessageBytes).startsWith("Demo message; ts:"));
    }

}
