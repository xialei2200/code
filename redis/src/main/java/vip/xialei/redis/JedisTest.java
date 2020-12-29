package vip.xialei.redis;

import redis.clients.jedis.Jedis;

public class JedisTest {
  public static void main(String[] arg) {
  }

  public static void normal() {
    Jedis jedis = new Jedis("127.0.0.1",6379);
    jedis.auth("QAZwsx");
    String value = jedis.get("key");
    jedis.close();
  }
}
