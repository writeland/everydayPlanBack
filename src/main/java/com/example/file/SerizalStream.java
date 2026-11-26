package com.example.file;


import java.io.*;

class Employee implements java.io.Serializable {
    public String name;
    public String address;
    public transient int age; // transient瞬态修饰成员,不会被序列化
    public void addressCheck() {
        System.out.println("Address check : " + name + " -- " + address);
    }
}

public class SerizalStream {
    public static void main(String[] args) {
//        Employee e = new Employee();
//        e.name = "zhangsan";
//        e.address = "guangzhou";
//        e.age = 20;
//        try {
//// 创建序列化流对象
//            ObjectOutputStream out = new ObjectOutputStream(new
//                    FileOutputStream("employee.txt"));
//// 写出对象
//            out.writeObject(e);
//// 释放资源
//            out.close();
//            System.out.println("序列化完成"); // 姓名，地址被序列化，年龄没有被序列化。
//        } catch(IOException i) {
//            i.printStackTrace();
//        }


        Employee eO = null;
        try {
// 创建反序列化流
            FileInputStream fileIn = new FileInputStream("employee.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
// 读取⼀个对象
            eO = (Employee) in.readObject();
// 释放资源
            in.close();
            fileIn.close();
        }catch(IOException i) {
// 捕获其他异常
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
// 捕获类找不到异常
            System.out.println("Employee class not found");
            c.printStackTrace();
            return;
        }
// ⽆异常,直接打印输出
        System.out.println("Name: " + eO.name); // zhangsan
        System.out.println("Address: " + eO.address); // beiqinglu
        System.out.println("age: " + eO.age); // 0
        eO.addressCheck();
    }
}
