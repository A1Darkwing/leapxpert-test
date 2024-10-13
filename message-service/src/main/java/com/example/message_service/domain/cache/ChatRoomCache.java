package com.example.message_service.domain.cache;

import com.example.purchase_prepaid_data_common.cache.ChatRoomMap;
import com.example.purchase_prepaid_data_common.util.JsonUtil;
import org.redisson.api.RBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ChatRoomCache {

    private static final Logger logger = LoggerFactory.getLogger(ChatRoomCache.class);

    private static final String CHAT_ROOM_ENTITY = "chatRoom|";

    @Value("${app.chatroom.expire.duration}")
    private String expireDurationInMin;

    @Autowired
    private RedisCache redisCache;

    public String getChatRoomEntityKey(String id) {
        return CHAT_ROOM_ENTITY + id;
    }

    public ChatRoomMap getChatRoomMap(String id) {
        RBucket<ChatRoomMap> bucket = redisCache.getClient()
                .getBucket(getChatRoomEntityKey(id));
        return bucket.get();
    }

    public boolean saveChatRoomMap(ChatRoomMap chatRoomMap) {
        try {
            RBucket<ChatRoomMap> bucket = redisCache.getClient()
                    .getBucket(getChatRoomEntityKey(chatRoomMap.getId()));

            // Sử dụng trySet để chỉ lưu nếu key chưa tồn tại
            boolean isSet = bucket.trySet(chatRoomMap, Long.parseLong(expireDurationInMin), TimeUnit.MINUTES);

            if (!isSet) {
                // Nếu trySet trả về false, nghĩa là key đã tồn tại
                logger.warn("saveChatRoomMap#WARNING Key already exists: {}", chatRoomMap.getId());
                return false;
            }
        } catch (Exception ex) {
            // Log lỗi nếu có
            logger.error("saveChatRoomMap#ERROR entity={}", JsonUtil.toJsonString(chatRoomMap), ex);
            return false;
        }
        return true;
    }

}

