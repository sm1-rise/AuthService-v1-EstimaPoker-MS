package br.com.samuel.martins.AuthService_v1.infra.rabbitMQ;

import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto.EmailBodyDto;
import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto.EmailPinDto;
import br.com.samuel.martins.AuthService_v1.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Value("${queue.name}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public EmailSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendAlertPasswordChange(User user) throws JsonProcessingException {
        var emailBody = new EmailBodyDto(user.getEmail(), user.getUsername(), user.getEmail(), "Changed Password", "Your Password was changed");

        String jsonMessage = objectMapper.writeValueAsString(emailBody);
        Message message = MessageBuilder
                .withBody(jsonMessage.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        rabbitTemplate.convertAndSend("", queueName, message);
    }


    public void sendEmail(User user) throws JsonProcessingException {
        var emailBodyDto = new EmailBodyDto(user.getId(), user.getUsername(), user.getEmail(),
                "EstimaPoker", "Thank you for subscription in EstimaPoker");

        // Serializa o objeto em JSON
        String jsonMessage = objectMapper.writeValueAsString(emailBodyDto);

        // Configura o conteúdo da mensagem
        Message message = MessageBuilder
                .withBody(jsonMessage.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();

        // Envia a mensagem para a fila
        rabbitTemplate.convertAndSend("", queueName, message);
    }

    public void SendPin(EmailPinDto dto) throws JsonProcessingException {
        var emailBodyDto  = new EmailBodyDto(null,null,dto.email(),"Acesso via Token", dto.pin());
        String jsonMessage = objectMapper.writeValueAsString(emailBodyDto);
        Message message = MessageBuilder
                .withBody(jsonMessage.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        rabbitTemplate.convertAndSend("", queueName, message);
    }









}
