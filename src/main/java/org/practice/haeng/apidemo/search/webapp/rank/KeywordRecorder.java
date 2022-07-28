package org.practice.haeng.apidemo.search.webapp.rank;

import java.util.Date;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.practice.haeng.apidemo.search.common.keyword.KeywordGenerator;
import org.practice.haeng.apidemo.search.common.model.KeywordEvent;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.stereotype.Component;

@Component
public class KeywordRecorder {

    private final KafkaOperations<String, KeywordEvent> kafkaOperations;
    private final KeywordGenerator keywordGenerator;

    public KeywordRecorder(
        KafkaOperations<String, KeywordEvent> kafkaOperations, KeywordGenerator keywordGenerator) {
        this.kafkaOperations = kafkaOperations;
        this.keywordGenerator = keywordGenerator;
    }

    public void recordKeyword(String keyword) {
        KeywordEvent keywordEvent = new KeywordEvent();
        keywordEvent.setKeyword(keywordGenerator.refineKeyword(keyword));
        keywordEvent.setOriginKeyword(keyword);
        keywordEvent.setUpdateDate(new Date());

        ProducerRecord<String, KeywordEvent> record = new ProducerRecord<>("keyword-count", keywordEvent.getKeyword(), keywordEvent);
        kafkaOperations.send(record);
    }
}
