package com.hoya.service.commons.zk;

import com.hoya.service.util.ProductSoutOutMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductSoldOutWatcher implements Watcher {

    @Autowired
    private ZkApi zkApi;

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            try {
                String path = watchedEvent.getPath();
                String soldOutFlag = zkApi.getData(path, this);
                log.info("zookeeper数据节点修改变动,path={},value={}", path, soldOutFlag);
                if ("false".equals(soldOutFlag)) {
                    // 本机清除售完标记
                    String productId = path.substring(path.lastIndexOf("/") + 1);
                    ProductSoutOutMap.clearSoldOut(Long.parseLong(productId));
                }
            } catch (Exception e) {
                log.error("zookeeper数据节点监听事件异常", e);
            }
        }
    }
}
