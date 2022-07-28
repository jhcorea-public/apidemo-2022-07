package org.practice.haeng.apidemo.search.consumerapp.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.practice.haeng.apidemo.search.common.model.KeywordEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KeywordCountConsumerConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KeywordEvent> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KeywordEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.setConcurrency(10);
        factory.getContainerProperties().setAckMode(AckMode.BATCH);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, KeywordEvent> consumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 2000);
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(KeywordEvent.class, false));
    }
}
