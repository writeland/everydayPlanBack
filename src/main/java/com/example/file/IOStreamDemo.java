package com.example.file;

import java.io.FileInputStream;
import java.io.IOException;

public class IOStreamDemo {
    public static void main(String[] args) throws IOException {
        // 使⽤File对象创建流对象
//        File file = new File("a.txt");
//        FileOutputStream fos = new FileOutputStream(file);
// 使⽤⽂件名称创建流对象
//        FileOutputStream fos = new FileOutputStream("b.txt");
// 使⽤⽂件名称创建流对象
//        FileOutputStream fos = new FileOutputStream("fos.txt");
//// 写出数据：虽然参数为int类型四个字节，但是只会保留⼀个字节的信息写出
//        fos.write(97); // 写出第1个字节
//        fos.write(98); // 写出第2个字节
//        fos.write(99); // 写出第3个字节
//// 关闭资源
//        fos.close();
//// 使⽤⽂件名称创建流对象
//        FileOutputStream fos = new FileOutputStream("fos.txt");
//// 字符串转换为字节数组
//        byte[] b = "你好中国".getBytes();
//// 写出字节数组数据
//        fos.write(b);
//// 关闭资源
//        fos.close();
//// 使⽤⽂件名称创建流对象
//        FileOutputStream fos = new FileOutputStream("fos.txt");
//// 字符串转换为字节数组
//        byte[] b = "abcde".getBytes();
//// 写出从索引2开始，2个字节。索引2是c，两个字节，也就是cd。
//        fos.write(b,2,2);
//// 关闭资源
//        fos.close();
        // 使⽤⽂件名称创建流对象
//        FileInputStream fis = new FileInputStream("read.txt");
//// 读取数据，返回⼀个字节
//        int read = fis.read();
//        System.out.println((char) read);
//        read = fis.read();
//        System.out.println((char) read);
//        read = fis.read();
//        System.out.println((char) read);
//        read = fis.read();
//        System.out.println((char) read);
//        read = fis.read();
//        System.out.println((char) read);
//// 读取到末尾,返回-1
//        read = fis.read();
//        System.out.println( read);
//// 关闭资源
//        fis.close();
// 使⽤⽂件名称创建流对象
//        FileInputStream fis = new FileInputStream("read.txt");
//// 定义变量，保存数据
//        int b;
//// 循环读取
//        while ((b = fis.read())!=-1) {
//            System.out.print((char)b);
//        }
//// 关闭资源
//        fis.close();
//// 使⽤⽂件名称创建流对象.
        FileInputStream fis = new FileInputStream("read.txt"); // ⽂件中为abcde
// 定义变量，作为有效个数
        int len;
// 定义字节数组，作为装字节数据的容器
        byte[] b = new byte[2];
// 循环读取
        while (( len= fis.read(b))!=-1) {
// 每次读取后,把数组的有效字节部分，变成字符串打印
            System.out.println(new String(b,0,len));// len 每次读取的有效字节个数
        }
// 关闭资源
        fis.close();
    }
}
