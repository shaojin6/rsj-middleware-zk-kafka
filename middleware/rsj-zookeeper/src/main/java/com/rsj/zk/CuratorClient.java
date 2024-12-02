package com.rsj.zk;
import java.io.File;
import java.util.Properties;
import javax.naming.ConfigurationException;
import javax.security.auth.login.ConfigurationSpi;
import org.apache.curator.RetryPolicy;
import org.apache.curator.SessionFailedRetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ClientCnxn;
import org.apache.zookeeper.data.Stat;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.zookeeper.data.Stat;

public class CuratorClient { 
    private CuratorFramework client;
    public CuratorClient() {
        Configurations configs = new Configurations();
        PropertiesConfiguration config;
        
        try {
            //从resources目录下加载配置文件
            config = configs.properties(new File("src/main/resources/application.properties"));
        } catch (ConfigurationException e) {
            System.err.println("Failed to load config file:" + e.getMessage());
            throw new RuntimeException(e);
        }
        //从配置文件中获取zookeeper的连接字符串
        String zookeeperConnectionString = config.getString("zookeeper.connection.string");
        if ((zookeeperConnectionString == null) || (zookeeperConnectionString.isEmpty())) {
            System.err.println("zookeeper connection is not failed");
            throw new RuntimeException("Zookeeper connection string is not set in the configuration file.");
        }
        System.out.println("Using zookeeper connection string: " + zookeeperConnectionString);
        
        //从配置文件中获取重试策略参数
        Integer baseSleepTimeMs = config.getInteger("retry.base.sleep.time.ms");
        Integer maxRetries = config.getInteger("retry.max.retries");
        
        if (baseSleepTimeMs == null) {
            System.err.println("Base sleep time is not supported");
            throw new RuntimeException("Zookeeper connection string is not set in the configuration file.");
        }
        
        if (maxRetries == null) {
            System.err.println("Max retries is not supported");
            throw new RuntimeException("Zookeeper connection string is not set in the configuration file.");
        }
        System.out.println("Using retry policy: base sleep time = " + baseSleepTimeMs + " ms, max retries = " + maxRetries);
        
        // 重试，从配置文件中读取
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        
        //创建客户端实例
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
        
        //启动客户端
        client.start();

        // 成功
        System.out.println("Connected to Zookeeper successfully!");
    }
    public void createNode(String path, byte[] data) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (Stat == null) {
            client.create().creatingParentsIfNeeded().forPath(path, data);
            System.out.println("Created node at path: " + path);
        } else {
            System.out.println("node already exists at path: " + path);

        }
    }
     
    public void deleteNode(String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
            System.out.println("Deleted node at path: " + path);
    }else {
        System.out.println("node does not exists at path: " + path);
    }
}

    public byte[] getNodeData(String path) throws Exception {
        // 检查节点是否存在
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            // 如果节点存在，则获取节点数据
            return client.getData().forPath(path);
        } else {
            System.out.println("Node does not exist at path: " + path);
            return null; // 或者你可以选择抛出一个自定义异常
        }
    }
    public List<String> getChildren(String path) throws Exception {
        // 检查节点是否存在
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            // 如果节点存在，则获取子节点列表
            return client.getChildren().forPath(path);
        } else {
            System.out.println("Node does not exist at path: " + path);
            return null; // 或者你可以选择抛出一个自定义异常
        }
    }
    public void close() {
        if (client != null) {
            client.close();
        }
    }
    public static void main(String[] args) {
        CuratorClient curatorClient = new CuratorClient();

        try {
            // 创建节点
            curatorClient.createNode("/test/node1", "data1".getBytes());

            // 获取节点数据
            byte[] data = curatorClient.getNodeData("/test/node1");
            if (data != null) {
                System.out.println("Node data: " + new String(data));
            }

            // 获取子节点
            List<String> children = curatorClient.getChildren("/test");
            if (children != null) {
                System.out.println("Children of /test: " + children);
            }

            // 删除节点
            curatorClient.deleteNode("/test/node1");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            curatorClient.close();
        }
    }
}

    public static void main(String[] args) {
        CuratorClient curatorClient = new CuratorClient();

        try {
            // 批量创建节点
            Map<String, byte[]> nodes = Map.of(
                    "/test/node1", "data1".getBytes(),
                    "/test/node2", "data2".getBytes(),
                    "/test/node3", "data3".getBytes()
                    // 添加更多节点...
            );
            curatorClient.createNodes(nodes);

            // 获取节点数据
            List<String> paths = List.of("/test/node1", "/test/node2", "/test/node3");
            List<byte[]> data = curatorClient.getNodeData(paths);
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) != null) {
                    System.out.println("Node data at path " + paths.get(i) + ": " + new String(data.get(i)));
                }
            }

            // 获取子节点
            List<List<String>> childrenList = curatorClient.getChildren(paths);
            for (int i = 0; i < childrenList.size(); i++) {
                if (childrenList.get(i) != null) {
                    System.out.println("Children of " + paths.get(i) + ": " + childrenList.get(i));
                }
                //遍历节点
                stringList.forEach(System.out::println);
            }

            // 删除节点
            curatorClient.deleteNodes(paths);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            curatorClient.close();
        }
    }
}