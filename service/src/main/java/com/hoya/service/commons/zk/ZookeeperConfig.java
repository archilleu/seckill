package com.hoya.service.commons.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Configuration
public class ZookeeperConfig {

    @Value("${zookeeper.address}")
    private String address;

    @Value("${zookeeper.timeout}")
    private int timeout;

    @Bean
    public ZooKeeper zkClient() {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper(address, timeout, watchedEvent -> {
                if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                    log.info("初始化zookeeper连接成功......");
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();
            log.info("zookeeper连接状态:{}", zooKeeper.getState());
            return zooKeeper;
        } catch (Exception e) {
            log.error("初始化zookeeper连接失败");
        }

        return null;
    }
}
