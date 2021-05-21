package railway.application.system.service.emailService;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import railway.application.system.models.response.Message;

@Service
public class RabbitMQService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${application.rabbitmq.exchange}")
    private String exchange;

    @Value("${application.rabbitmq.routingkey}")
    private String routingkey;

    public void send(Message message) {
        rabbitTemplate.convertAndSend(exchange, routingkey, message);
        System.out.println("Send msg = " + message);

    }
}