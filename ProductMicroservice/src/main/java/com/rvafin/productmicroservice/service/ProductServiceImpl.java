package com.rvafin.productmicroservice.service;

import com.rvafin.productmicroservice.service.dto.CreateProductDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import com.rvafin.core.ProductCreatedEvent;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService{

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductDTO createProductDTO) throws ExecutionException, InterruptedException {
        //TODO save DB
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent(createProductDTO.getTitle(), createProductDTO.getPrice(), createProductDTO.getQuantity(), productId);
        ProducerRecord<String, ProductCreatedEvent> record
                = new ProducerRecord<>(
                    "product-created-events-topic"
                    , productId
                    , productCreatedEvent);

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send(record).get();
        LOGGER.info("Topic {}", result.getRecordMetadata().topic());
        LOGGER.info("Partition {}", result.getRecordMetadata().partition());
        LOGGER.info("Offset {}", result.getRecordMetadata().offset());
        return productId;
    }
}
