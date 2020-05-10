package com.wzp.module.user.controller;

import lombok.SneakyThrows;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import static sun.misc.VM.getState;

public class Demo {
    static Lock lock = new ReentrantLock();
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

//    public static void main(String[] args) throws InterruptedException {
//        Runnable runnable = new Runnable() {
//            public void run() {
//                try {
//                    new Demo().test(Thread.currentThread());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        Thread thread1 = new Thread(runnable);
//        Thread thread2 = new Thread(runnable);
//        thread1.start();
//        Thread.sleep(500);
//        thread2.start();
//        Thread.sleep(1000);
//        thread2.interrupt();
//    }

    public void test(Thread thread) throws InterruptedException {
        lock.lock();
        System.out.println(thread.getName() + " 想获取锁");
        try {
            lock.lockInterruptibly();
            System.out.println(thread.getName() + " 获取到了锁");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " 抛出InterruptedException异常");
        } finally {
            System.out.println(thread.getName() + " 释放了锁");
            lock.unlock();
        }
    }

    public void test1() throws InterruptedException {
        Semaphore semaphore = new Semaphore(5);
        semaphore.acquire();
        // todo: do something
        semaphore.release();

        CountDownLatch countDownLatch = new CountDownLatch(5);

        countDownLatch.await();
        countDownLatch.countDown();

    }

    //    public static void main(String[] args) {
//        Runnable action = new Runnable() {
//            @Override
//            public void run() {
//                // todo: 批量执行SQL
//                System.out.println("批量执行了SQL");
//            }
//        };
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,action);
//        Runnable task = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // todo：将SQL缓存起来
//                    Thread.sleep(1000);
//                    System.out.println("生成SQL完成");
//                    cyclicBarrier.await();
//                } catch (InterruptedException | BrokenBarrierException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        for (int i = 0; i < 5; i++) {
//            Thread thread = new Thread(task);
//            thread.start();
//        }
//    }
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("Aa","abc");
        map.put("BB","abc");
        map.put("BB","abcd");
    }
}
