package com.rsj.kafka.test.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerTest {
    public static void main(String[] args) {
        // 创建配置对像

        Map<String, Object> consumerConfig = new HashMap<>();
 //      consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,  "11.0.1.110:32092,11.0.1.111:32093,11.0.1.112:32094");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,  "192.168.123.241:9092");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "rsj");

        // 创建消费者对像
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig);
        //  订阅主题
        consumer.subscribe(Collections.singletonList("test4"));
        //  从kafka的主题中获取数据
        //        消费者从kafka中拉取数据
        while (true) {
            final ConsumerRecords<String, String> datas = consumer.poll(100);
            for (ConsumerRecord<String, String> data : datas) {
                System.out.println(data);
            }
        }
        // 关闭消费者对像
   //     consumer.close();
    }
}
