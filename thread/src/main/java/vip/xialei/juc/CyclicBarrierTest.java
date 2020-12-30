package vip.xialei.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        new Thread(()->{
            log.info("t1 开始执行");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            log.info("t1 继续执行");
        },"t1").start();
        new Thread(()->{
            log.info("t2 开始执行");
            try {
                Thread.sleep(1000 * 5 );
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            log.info("t2 继续执行");
        },"t2").start();
    }
}
