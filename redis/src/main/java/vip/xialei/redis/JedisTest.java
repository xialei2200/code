package vip.xialei.redis;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JedisTest {
  public static void main(String[] arg) {
  }

  public static void normal() {
    // 指定Redis服务Host和port
    Jedis jedis = new Jedis("127.0.0.1",6379);
    // 密码
    jedis.auth("QAZwsx");
    // 操作
    String value = jedis.get("key");
    // 关闭连接
    jedis.close();
  }

  public static void pool() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(5);
    jedisPoolConfig.setMaxTotal(15);
    JedisPool jedisPool = new JedisPool(jedisPoolConfig,"127.0.0.1",6379,2000, "password");
    Jedis jedis = jedisPool.getResource();
    // 操作
    String value = jedis.get("key");
    // 这里是归还连接
    jedis.close();
    // 关闭连接池
    jedisPool.close();
  }

  public static void sentinels() {
    Set<String> sentinels = new HashSet<>();
    sentinels.add("127.0.0.1:6380");
    sentinels.add("127.0.0.1:6381");
    sentinels.add("127.0.0.1:6382");
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxIdle(5);
    config.setMaxTotal(20);
    JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster",sentinels,config,2000,"password");
    Jedis jedis = jedisSentinelPool.getResource();
    jedis.get("key");
    jedis.close();
    jedisSentinelPool.close();
  }

  public static void shared() {
    //连接池信息
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(15);
    jedisPoolConfig.setMaxIdle(5);
    jedisPoolConfig.setTestOnBorrow(true);
    //分片信息
    List<JedisShardInfo> list = new ArrayList<>();
    list.add(new JedisShardInfo("127.0.0.1",6380));
    list.add(new JedisShardInfo("127.0.0.1",6381));
    list.add(new JedisShardInfo("127.0.0.1",6382));
    ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig,list);
    ShardedJedis jedis = shardedJedisPool.getResource();
    String value = jedis.get("key");
    jedis.close();
    shardedJedisPool.close();
  }

  public static void cluster() {
    Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//Jedis Cluster will attempt to discover cluster nodes automatically
    jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6380));
    jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6381));
    jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6382));
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(5);
    jedisPoolConfig.setMaxTotal(15);
    jedisPoolConfig.setMinIdle(0);
    jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
    jedisPoolConfig.setTestOnBorrow(true);
    JedisCluster jc = new JedisCluster(jedisClusterNodes, 2000, 1000, 17, "password",jedisPoolConfig);
    jc.set("foo", "bar");
    String value = jc.get("foo");
  }
}
