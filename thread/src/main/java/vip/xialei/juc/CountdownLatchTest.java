package vip.xialei.juc;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟王者荣耀：等待所有玩家加载完成后，才开始游戏
 */
public class CountdownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger integer = new AtomicInteger();
        ExecutorService service = Executors.newFixedThreadPool(10, (r) -> {
            return new Thread(r, "t" + integer.incrementAndGet());
        });
        CountDownLatch countdownLatch = new CountDownLatch(10);
        String[] all = new String[10];
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            int x = i;
            service.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    try {
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[x] = Thread.currentThread().getName() + "("+ (j+1) + "%)";
                    System.out.print("\r" + Arrays.toString(all));
                }
                countdownLatch.countDown();
            });
        }
        countdownLatch.await();
        System.out.println("\n游戏开始...");
        service.shutdown();
    }
}
