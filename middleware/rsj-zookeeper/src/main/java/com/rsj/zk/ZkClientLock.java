/*
package com.rsj.zk;

import javax.xml.crypto.Data;

public class ZkClientLock {
    @Test
    public void lock(){
        ZkClientLock zkClient = new ZkClient("192.168.123.240:31281");
        boolean f = zkClient.exists("/mylocak");
        if (!f) {// 
            try {
                zkClient.createEphemeral("/mylocak", data: "测试分布式锁");
                System.out.println("创建锁成功");
                System.out.println("开始处理业务");
            } catch (Exception e) {
                System.out.println("获取锁失败");
        }finally {
                zkClient.close();
            }
        
    }else{
            System.out.println("获取锁失败,等待释放锁");
            Thread.sleep(1000);
            lock();
        }

}
*/
