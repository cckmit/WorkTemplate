package com.blaze.common.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author CC ccheng0725@outlook.com
 * @date 2020-05-14 23:04
 */
@Component
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * 注入redisTemplate bean
     */
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 设定缓存失效时间
     *
     * @param key  键
     * @param time 时间，毫秒数
     * @return 操作结果
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                return redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (NullPointerException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }
    // ============================String(字符串)=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 根据key模糊匹配
     *
     * @param key key
     * @return 缓存个数
     */
    public Set<String> getLikeKeys(String key) {
        return redisTemplate.keys("*" + key + "*");
    }

    /**
     * 递增/递减
     *
     * @param key   键
     * @param delta 大于0递增，小于0递减
     * @return 值
     */
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    // ================================Hash(哈希)=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hashGet(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hashPut(String key, Map<String, String> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hashPut(String key, Map<String, String> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hashPut(String key, String item, String value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hashPut(String key, String item, String value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 进行批量查找元素集合
     *
     * @param key   键
     * @param items 元素集合
     * @return 集合内容
     */
    public List<Object> hashMGet(String key, List<String> items) {
        try {
            List<Object> data = new ArrayList<>();
            data.addAll(items);
            return redisTemplate.opsForHash().multiGet(key, data);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hashDelete(String key, String... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hashHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增/递减 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   递增/递减因子，大于0递增，小于0递减
     * @return 递增/递减结果
     */
    public double hashIncrement(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    // ============================Set(集合)=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set集合
     */
    public Set<String> setMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean setIsMember(String key, String value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long setAdd(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间，毫秒
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long setAdd(String key, long time, String... values) {
        try {
            long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return 缓存长度
     */
    public long setSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, String... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }
    // ===============================List(列表)=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return list缓存内容
     */
    public List<String> listRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list缓存长度
     */
    public long listSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 缓存对象
     */
    public String listIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 放入个数
     */
    public long listPush(String key, String value) {
        try {
            return redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return 放入个数
     */
    public long listPush(String key, String value, long time) {
        try {
            long listLength = listPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return listLength;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key  键
     * @param list 值
     * @return 放入个数
     */
    public long listPush(String key, List<String> list) {
        try {
            return redisTemplate.opsForList().rightPushAll(key, list);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key  键
     * @param list 值
     * @param time 时间(秒)
     * @return 放入个数
     */
    public long listPush(String key, List<String> list, long time) {
        try {
            long listLength = listPush(key, list);
            if (time > 0) {
                expire(key, time);
            }
            return listLength;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 修改结果
     */
    public boolean listUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, String value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    // ===============================SortList(排序列表)=================================

    /**
     * 进行一组元素追加
     *
     * @param key    键
     * @param member 元素
     * @param score  追加值
     */
    public void zincrby(String key, String member, double score) {
        try {
            redisTemplate.opsForZSet().incrementScore(key, member, score);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 进行一组添加或替换对象
     *
     * @param key    键
     * @param member 元素
     * @param score  分数
     */
    public void zAdd(String key, String member, double score) {
        try {
            redisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取元素的排行榜
     *
     * @param key    键
     * @param member 元素
     * @return 排行榜索引
     */
    public Long zRank(String key, String member) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, member);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0L;
        }
    }

    /**
     * 获取排行榜总人数
     *
     * @param key 赛场信息
     * @return
     */
    public Long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0L;
        }
    }

    /**
     * 获取元素的分数
     *
     * @param key    键
     * @param member 元素
     * @return 排行榜分数
     */
    public Double zScore(String key, String member) {
        try {
            return redisTemplate.opsForZSet().score(key, member);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return 0.0;
        }
    }

    /**
     * 获取排行榜前start - end
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 排行对象
     */
    public Set<ZSetOperations.TypedTuple<String>> zRankWithScore(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * 提供get方法是可以让用户使用没有封装的方法
     *
     * @return redisTemplate实例
     */
    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}