package com.rvafin.emailnotification.handler;

import com.rvafin.core.ProductCreatedEvent;
import com.rvafin.emailnotification.exception.NonRetryableException;
import com.rvafin.emailnotification.exception.RetryableException;
import com.rvafin.emailnotification.persistence.entity.ProcessedEventEntity;
import com.rvafin.emailnotification.persistence.repo.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {


    private final RestTemplate restTemplate;
    private final ProcessedEventRepository processedEventRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductCreatedEventHandler(
            RestTemplate restTemplate
            , ProcessedEventRepository processedEventRepository
    ) {
        this.restTemplate = restTemplate;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    @KafkaHandler
    public void handle(
            @Payload ProductCreatedEvent productCreatedEvent
            , @Header("messageId") String messageId
            , @Header(KafkaHeaders.RECEIVED_KEY) String messageKey
    ) {
        LOGGER.info("Receive event {}", productCreatedEvent.getTitle());
        String url = "http://localhost:8090/response/200";
        Optional<ProcessedEventEntity> processedEventEntity = processedEventRepository.findByMessageId(messageId);
        if (processedEventEntity.isPresent()){
            LOGGER.info("Duplicate message id: {}", messageId);
            return;
        }
        try {
            ResponseEntity<String> response =  restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Receive response {}", response.getBody());
            }
        }catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
            throw new RetryableException(e);
        }catch (HttpServerErrorException e){
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e);
        }

        try {
            processedEventRepository.save(new ProcessedEventEntity(messageId, productCreatedEvent.getProductId()));
        }catch (DataIntegrityViolationException e){
            LOGGER.error(e.getMessage());
            throw new NonRetryableException(e);
        }

    }
}
