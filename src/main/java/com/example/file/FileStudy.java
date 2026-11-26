package com.example.file;

import java.io.File;
import java.io.IOException;

public class FileStudy {
    public static void main(String[] args) throws IOException {
        //⽂件的相关操作
        File f = new File("d:/aaa/bbb.java");
        System.out.println("⽂件绝对路径:"+f.getAbsolutePath());
        System.out.println("⽂件构造路径:"+f.getPath());
        System.out.println("⽂件名称:"+f.getName());
        System.out.println("⽂件⻓度:"+f.length()+"字节");
        //创建⽂件 createNewFile()
        File file=new File("d:\\file.txt");
        //System.out.println(file.toString());
        if(!file.exists()) {
            boolean b=file.createNewFile();
            System.out.println("创建结果:"+b);
        }
        System.out.println("是否⽂件:"+file.isFile());
        //⽂件夹的相关操作
        File f2 = new File("d:/aaa");
        System.out.println("⽬录绝对路径:"+f2.getAbsolutePath());
        System.out.println("⽬录构造路径:"+f2.getPath());
        System.out.println("⽬录名称:"+f2.getName());
        System.out.println("⽬录⻓度:"+f2.length());
        //创建⽂件夹
        File dir=new File("d:\\aaa\\bbb\\ccc");
        System.out.println(dir.toString());
        if(!dir.exists()) {
//dir.mkdir();//只能创建单级⽬录
            System.out.println("创建结果:"+dir.mkdirs());//创建多级⽬录
        }
        System.out.println("是否⽂件夹:"+dir.isDirectory());
//遍历⽂件夹
        File dir2=new File("d:\\java学习");
        String[] files=dir2.list();
        System.out.println("--------------------------------");
        for (String string : files) {
            System.out.println(string);
        }
    }
}
