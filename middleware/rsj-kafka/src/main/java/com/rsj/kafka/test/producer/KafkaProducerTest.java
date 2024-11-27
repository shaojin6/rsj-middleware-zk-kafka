package com.rsj.kafka.test.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaProducerTest {
    public static void main(String[] args) {

        //  创建配置对像

        Map<String, Object> configMap = new HashMap<>();
//        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "11.0.1.110:32092,11.0.1.111:32093,11.0.1.112:32094");
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.123.241:9092");
//        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "11.0.1.112:32094");
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //创建生产者对像、设定泛型、类型约束
//        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configMap);
        KafkaProducer<String, String> producer = new KafkaProducer<>(configMap);

        //创建数据--
//        ProducerRecord<String, String> record = new ProducerRecord<>(
//                topic: "test", key: "key", value: "value"
//        );
//        //数据发送到Kafka
//        producer.send(record);

        for ( int i = 0; i < 10; i++ ) {
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    "test4", "key" + i, "value" + i
            );
            producer.send(record);
        }
        // 关闭生产者对像
        producer.close();
    }
}
