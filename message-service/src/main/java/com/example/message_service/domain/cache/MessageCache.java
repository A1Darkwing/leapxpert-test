package com.example.message_service.domain.cache;

import com.example.purchase_prepaid_data_common.cache.MessageMap;
import com.example.purchase_prepaid_data_common.util.JsonUtil;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MessageCache {

    private static final Logger logger = LoggerFactory.getLogger(MessageCache.class);

    private static final String MESSAGE_ENTITY = "messageImport|";

    @Value("${app.message-import.expire.duration}")
    private String expireDurationInMin;

    @Autowired
    private RedisCache redisCache;

    private String getMessageImportEntityKey(String id) {
        return MESSAGE_ENTITY + id;
    }

    public MessageMap getChatRoomMap(String id) {
        RBucket<MessageMap> bucket = redisCache.getClient()
                .getBucket(getMessageImportEntityKey(id));
        return bucket.get();
    }

    public boolean saveImportMessage(MessageMap messageMap) {
        try {
            RBucket<MessageMap> bucket = redisCache.getClient()
                    .getBucket(getMessageImportEntityKey(messageMap.getId()));
            bucket.set(messageMap, Long.parseLong(expireDurationInMin), TimeUnit.MINUTES);
        } catch (Exception ex) {
            logger.error("saveChatRoomMap#ERROR entity={}", JsonUtil.toJsonString(messageMap), ex);
            return false;
        }
        return true;
    }

}

