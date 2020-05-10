package com.wzp.module.user.controller;

import javax.sound.midi.Soundbank;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class Demo1 {

    private static ThreadPoolExecutor threadPoolExecutor = null;

    // new ArrayBlockingQueue<Runnable>(10) 有界队列
    static {
        threadPoolExecutor = new ThreadPoolExecutor(5, 10,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("有任务被丢弃了");
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger i = new AtomicInteger();
//        for (int i = 0; i < 21; i++) {
//            threadPoolExecutor.execute(task(i));
//        }
//        System.out.println("线程池里当前存活线程数：" + threadPoolExecutor.getActiveCount());
//        System.out.println("缓冲队列中剩余位置：" + threadPoolExecutor.getQueue().remainingCapacity());
//        Thread thread = new Thread(() -> {
//            try {
//                i.addAndGet(1);
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//        Thread.sleep(1000);
//        System.out.println(thread.getState());
        new Demo1().waitAndNotify();
    }

    public static Runnable task(int i) {
        return () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前第" + i + "个任务");
        };
    }

    public void suspendAndResume() throws InterruptedException {
        Object o = new Object();
        Thread thread = new Thread(() -> {
            try {
                System.out.println("开始休眠");
                Thread.sleep(3000);
                System.out.println("准备调用suspend()");
                Thread.currentThread().suspend();
                System.out.println("唤醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        Thread.sleep(1000);
        System.out.println("准备调用resume()");
        // 这里由于有限调用了resume(),因此当线程一旦调用suspend()方法后，在本代码中就无法唤醒了
        thread.resume();
    }

    public void waitAndNotify() throws InterruptedException {
        // 这种方式对唤醒的调用顺序有要求
        Thread thread = new Thread(() -> {
            int i = 0;
            System.out.println("线程开始执行run方法");
            try {
                Thread.sleep(3000);
                synchronized (this) {
                    System.out.println("子线程开始休眠");
                    // 由于主线程先执行了notify()方法，因此在这里会造成死锁
                    this.wait();
                    System.out.println("子线程被唤醒");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("子线程被interrupt");
            }
        });
        thread.start();
        Thread.sleep(1000);
        synchronized (this) {
            this.notify();
            System.out.println("主线程唤醒子线程");
        }
        Thread.sleep(5000);
        thread.interrupt();
    }


    public void join() throws InterruptedException {

        Thread thread = new Thread(() -> {
            synchronized (this){
                System.out.println("begin sleep");
                LockSupport.park();

                System.out.println("==================");
                for (int i = 0; i < 5; i++) {
                    System.out.println("线程1 = " + i);
                }
            }

        });
        thread.start();
        thread.join();
        Thread.sleep(2000);
        System.out.println(thread.getState());
        LockSupport.unpark(thread);
        System.out.println("线程end");
        Thread.currentThread().stop();
    }

}
