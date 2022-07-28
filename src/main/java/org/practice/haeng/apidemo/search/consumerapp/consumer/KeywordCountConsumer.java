package org.practice.haeng.apidemo.search.consumerapp.consumer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.practice.haeng.apidemo.search.common.model.KeywordEvent;
import org.practice.haeng.apidemo.search.consumerapp.service.KeywordCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KeywordCountConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordCountConsumer.class);

    private KeywordCountService keywordCountService;

    public KeywordCountConsumer(
        KeywordCountService keywordCountService) {
        this.keywordCountService = keywordCountService;
    }

    @KafkaListener(id = "default"
            , topics = "keyword-count"
            , containerFactory = "listenerContainerFactory")
    public void process(List<ConsumerRecord<String, KeywordEvent>> records) {
        LOGGER.debug("[EVENT OCCUR] :: {}", records);
        keywordCountService.increaseKeywordCountBulk(records.stream().map(ConsumerRecord::value).collect(Collectors.toList()));
    }


}
