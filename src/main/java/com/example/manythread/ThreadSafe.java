package com.example.manythread;

class TicketRunnable implements Runnable {
    private int ticket=100;
    Object lock = new Object();
    //每个窗⼝卖票的操作
//窗⼝ 永远开启
    @Override
    public void run() {
        while(ticket>0){//有票可以卖
//出票操作
            synchronized(lock){//synchronized (this) {//this ---当前对象
                if(ticket>0){
//使⽤sleep模拟⼀下出票时间 //模拟⼀下出票的时间
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"正在卖票："+ticket--);
                }
            }
        }
    }
}

public class ThreadSafe {
    public static void main(String[] args) throws InterruptedException {
//        TicketRunnable t = new TicketRunnable();
//        Thread t1 = new Thread(t,"窗⼝1");
//        Thread t2 = new Thread(t,"窗⼝2");
//        Thread t3 = new Thread(t,"窗⼝3");
//        //3个窗⼝同时卖票
//        t1.start();
//        t2.start();
//        t3.start();
        MyList myList = new MyList();
//
        Thread t1 =new Thread(new Runnable() {
            @Override
            public void run() {
                myList.add("hello");
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                myList.add("world");
            }
        });
        t2.start();
        t1.join();
        t2.join();
        String[] str = myList.getStr();
        for (String s : str) {
            System.out.println("s:"+s);
        }

    }
}
