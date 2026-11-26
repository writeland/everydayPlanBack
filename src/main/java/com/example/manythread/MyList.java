package com.example.manythread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class MyList {
    //创建锁
    private Lock lock = new ReentrantLock();
    private String[] str = {"A","B","","",""};
    private int count = 2;
    public void add(String value){
//当没有锁的时候，会出现覆盖的情况
//        str[count] = value;
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        count++;
//        System.out.println(Thread.currentThread().getName()+"添加了"+value + "count值为" + count);
 lock.lock();
 try {
 str[count] = value;
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 count++;
 System.out.println(Thread.currentThread().getName()+"添加了"+value + "count值为" + (count-1));
 }finally {
 lock.unlock();
 }
    }
    public String[] getStr(){
        return str;
    }
}
