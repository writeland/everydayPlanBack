package com.example.manythread;

public class SleepDemo {
    public static void main(String[] args) {
        new SleepThread().start();

//      主线程
        for(int i = 0; i < 10; i++) {
            System.out.println("执行主流程~" + i);
        }
    }
}
