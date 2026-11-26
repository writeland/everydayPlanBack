package com.example.manythread;

public class SleepThread extends Thread{
    @Override
    public void run() {
        String[] names = new String[]{"zs","ls","ww","z6"};
        int index = (int)(Math.random()*4);
        for (int i = 3;i > 0;i--)
        {
            System.out.println("倒计时："+i);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("抽中学员为:"+names[index]);
    }
}
