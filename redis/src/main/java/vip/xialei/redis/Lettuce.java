package vip.xialei.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.assertj.core.api.Assertions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

public class Lettuce {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        reactive();
    }

    public static void testSetGet() throws ExecutionException, InterruptedException {
        // <1> 创建单机连接的连接信息
        RedisURI redisUri = RedisURI.builder()
                .withHost("172.16.17.200")
                .withPort(6379)
                .withPassword("umpredis")
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        // <2> 创建客户端
        RedisClient redisClient = RedisClient.create(redisUri);
        // <3> 创建线程安全的连接
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        // <4> 创建同步命令
        RedisAsyncCommands<String, String> redisCommands = connection.async();
        SetArgs setArgs = SetArgs.Builder.nx().ex(5);
        RedisFuture<String> result = redisCommands.set("key", "value", setArgs);
        Assertions.assertThat(result.get()).isEqualToIgnoringCase("OK");
        result = redisCommands.get("key");
        Assertions.assertThat(result.get()).isEqualTo("value");
        connection.close();
        redisClient.shutdown();
    }

    public static void reactive() throws ExecutionException, InterruptedException {
        // <1> 创建单机连接的连接信息
        RedisURI redisUri = RedisURI.builder()
                .withHost("172.16.17.200")
                .withPort(6379)
                .withPassword("umpredis")
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        // <2> 创建客户端
        RedisClient redisClient = RedisClient.create(redisUri);
        // <3> 创建线程安全的连接
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        // <4> 创建同步命令
        RedisReactiveCommands<String, String> reactive = connection.reactive();
        SetArgs setArgs = SetArgs.Builder.nx().ex(5);
        reactive.set("key", "value", setArgs).subscribe(v -> System.out.println(v));
        reactive.get("key").subscribe(v-> System.out.println(v));
        connection.close();
        redisClient.shutdown();
    }
}
