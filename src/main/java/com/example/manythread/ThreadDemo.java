package com.example.manythread;

class Task1 implements Runnable{
    @Override
    public void run() {
        for (int i = 0;i < 200;i++){
            System.out.println("A:"+i);
        }
    }
}
class Task2 implements Runnable{
    @Override
    public void run() {
        for (int i = 0;i < 10;i++){
            System.out.println("B:"+i);
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            Thread.yield();
        }
    }
}

class JoinThread extends Thread{
    public JoinThread(String name){
        super(name);
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"线程开始运⾏");
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+"-->⼦线程："+i);
        }
        System.out.println(Thread.currentThread().getName()+"线程结束运⾏");
    }
}

class DeamonThread extends Thread {
    @Override
    public void run() {
        for(int i=0;i<50;i++) {
            System.out.println(Thread.currentThread().getName()+"----------"+i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
//        继承
//        MyThread mt = new MyThread("新建线程");
//        mt.start();
//        for (int i = 0; i < 10; i++){
//            System.out.println("主线程：" + i);
//        }

//        重写Runnable方法
//        MyRunnable mr = new MyRunnable();
//
//        Thread t = new Thread(mr, "新建的线程");
//        t.start();
//        for(int i = 0; i < 20; i++) {
//            System.out.println("主线程：" + i);
//        }

//        new Thread(new Task2()).start();
//        new Thread(new Task1()).start();
//        System.out.println("主线程开始运⾏。。。");
//        JoinThread t1 = new JoinThread("新加⼊的线程");
//        t1.start();
// try {
// t1.join();
// } catch (InterruptedException e) {
// e.printStackTrace();
// }
//        System.out.println("主线程开始结束。。。");
        //创建线程(默认前台线程)
        DeamonThread d1=new DeamonThread();
//设置线程为守护线程
        d1.setDaemon(true);//主线程结束便结束了
        d1.start();
        for(int i=0;i<10;i++) {
            System.out.println("主线程:----------"+i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
