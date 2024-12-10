package com.rsj.zk;

import java.util.List;

import javax.xml.crypto.Data;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.shaded.com.google.common.net.HostAndPort;
import org.apache.zookeeper.CreateMode;

import jdk.jfr.Event;


public class CuratorClient {

    private static final String Cluster = "192.168.123.240:30181";
    private static final String ROOT_PATH = "curator";

    public static void main(String[] args) throws Exception {
        // 设置 jute.maxbuffer 系统属性
        System.setProperty("jute.maxbuffer", "10485760");
        //创建尝试等待连接时间和次数
        ExponentialBackoffRetry RetryPolicy = new ExponentialBackoffRetry(1000, 3);
        // 创建会话
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(Cluster)
                .sessionTimeoutMs(30 * 60 * 1000)
                .connectionTimeoutMs(30 * 1000)   // 连接超时30s
                .retryPolicy(RetryPolicy)
                .namespace(ROOT_PATH)
                .build();
        client.start();
        

         String nodePathPersistent = "/persistent";
         String nodePathEphemeral = "/ephemeral";
         
         //创建持久化节点,
         if (client.checkExists().forPath(nodePathPersistent) == null) {
             String PersistentNode = client.create().creatingParentContainersIfNeeded()
                     .withMode(CreateMode.PERSISTENT)
                     .forPath(nodePathPersistent, "PERSISTENTDATA".getBytes());
             System.out.println("创建的持久化节点是：/" + ROOT_PATH + PersistentNode);
         }else {
             System.out.println("持久化节点已存在：" + nodePathPersistent);
         }

         //创建临时节点
         String EphemeralNode = client.create().creatingParentContainersIfNeeded()
                 .withMode(CreateMode.EPHEMERAL)
                 .forPath(nodePathEphemeral, "EPHEMERALDATA".getBytes());
         System.out.println("创建的临时节点是：/" + ROOT_PATH + EphemeralNode);
         
         
        //获取数据内容
         byte[] bytesPersistent = client.getData().forPath(nodePathPersistent);
         System.out.println("持久化节点值为： " + new String(bytesPersistent));

         byte[] bytesEphemeral = client.getData().forPath(nodePathPersistent);
         System.out.println("临时节点值为： " + new String(bytesEphemeral));
         
         //监听数据变化       
         byte[] data = client.getData().usingWatcher((CuratorWatcher) event -> {
             System.out.println(event.getPath() + "的数据已更改");
         }).forPath(nodePathPersistent);
         System.out.println("当前持久化节点数据内容为：" + new String(data));
         
         // 更新数据内容
         client.setData().forPath(nodePathPersistent, "PERSISTENTDATAl".getBytes()); 
         byte[] newdata = client.getData().forPath(nodePathPersistent);
        System.out.println("更新数据为：" + new String(newdata));
        
         //获取子节点列表
         List<String> children = client.getChildren().forPath("/");
         System.out.println("curator的子节点列表:" + children);

         //删除节点
         if (client.checkExists().forPath("/") != null){
             client.delete().deletingChildrenIfNeeded().forPath("/");
         }
         System.out.println("删除节点成功");

/*         // 关闭 CuratorFramework 连接
         client.close();
         System.out.println("关闭连接成功");*/
     }
}