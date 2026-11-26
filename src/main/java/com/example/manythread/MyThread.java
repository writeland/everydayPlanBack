package com.example.manythread;

public class MyThread extends Thread{
    //定义指定线程名称的构造⽅法
    public MyThread(String name) {
//调⽤⽗类的String参数的构造⽅法，指定线程的名称
        super(name);
    }
    /**
     * 重写run⽅法，完成该线程执⾏的逻辑
     */
    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            System.out.println(getName()+"："+i);
        }
    }
}
