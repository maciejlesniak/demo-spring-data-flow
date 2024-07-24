package pl.sparkidea.springcloud.demo.demoprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableTestBinder
class DemoProcessorKafkaApplicationTests {

    @Autowired
    private InputDestination input;

    @Autowired
    private OutputDestination output;


    @Test
    void processorFunction_shouldUpecaseAndAddStaticString_whenStringGiven() {

        // given
        var inputMessage = new GenericMessage<>("hello");

        // when
        input.send(inputMessage);
        var receivedMessageBytes = output.receive().getPayload();

        // then
        assertEquals("HELLO -- additional string in lower case", new String(receivedMessageBytes));
    }

}
