package com.hoya.service.commons.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisClient {

    @Autowired
    private JedisPool jedisPool;

    public <T> boolean set(String key, T value, long expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            String str = beanToString(value);
            if (expire <= 0) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, expire, str);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void set(String key, String value) throws Exception {
        set(key, value);
    }

    public boolean set(String key, Object value) {
        return set(key, value);
    }

    public <T> boolean set(RedisKeyPrefix prefix, String key, T value) {
        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        long expireSeconds = prefix.expireSeconds();
        return set(realKey, value, expireSeconds);
    }

    public <T> T get(String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            String str = jedis.get(key);
            T t = stringToBean(str, clazz);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T get(RedisKeyPrefix prefix, String key, Class<T> clazz) {
        String realKey = prefix.getPrefix() + key;
        return get(realKey, clazz);
    }

    /**
     * <p>
     * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
     * </p>
     */
    public Long incr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.incr(key);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> Long incr(RedisKeyPrefix prefix, String key) {
        String realKey = prefix.getPrefix() + key;
        return incr(realKey);
    }

    public <T> Long decr(RedisKeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } catch (Exception e) {
            return 0L;
        }
    }

    public boolean delete(RedisKeyPrefix prefix, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return Integer.toString((Integer) value);
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return Long.toString((Long) value);
        } else {
            return JSON.toJSONString(value);
        }
    }

}
