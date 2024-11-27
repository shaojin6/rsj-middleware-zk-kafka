package com.rsj.kafka.test.admin;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminTopicTest {
    public static void main(String[] args) {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "11.0.1.110:32092,11.0.1.111:32093,11.0.1.112:32094");

        //TODO 管理员对像
        final Admin admin = Admin.create(configMap);
        //TODO 主题需要传递三个参数
        //  第一个参数表示主题的名称：字母，数字，点，下划线，中横线；
        //  第二参数表示主题分区的数量： int
        //  第三个参数表示主题分区副本的（因子）数量：short
        String topicName = "test1";
        int partitionCount = 1;
        short replicationCount= 1;
        NewTopic topic1 =new NewTopic(topicName, partitionCount, replicationCount);

        String topicName1 = "test2";
        int partitionCount1 = 2;
        short replicationCount1= 2;
        NewTopic topic2 =new NewTopic(topicName1, partitionCount1, replicationCount1);

//        String topicName2 = "test3";
//        Map<Integer, List<Integer>> map = new HashMap<>();
//        map.put(0, Arrays.asList(3, 1));
//        map.put(1, Arrays.asList(2, 3));
//        map.put(2, Arrays.asList(1, 2));
//        NewTopic topic3 = new NewTopic(topicName2, map);


        //TODO 创建主题
        final CreateTopicsResult result = admin.createTopics(
                Arrays.asList(topic1, topic2)
        );
        // TODO 关闭管理者对像
        admin.close();
    }
}

