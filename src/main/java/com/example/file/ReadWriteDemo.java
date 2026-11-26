package com.example.file;

import java.io.FileReader;
import java.io.IOException;

public class ReadWriteDemo {
    public static void main(String[] args) throws IOException {

        // 使⽤File对象创建流对象
//        File file = new File("a.txt");
//        FileReader fr = new FileReader(file);
//// 使⽤⽂件名称创建流对象
//        FileReader fr = new FileReader("b.txt");
// 使⽤⽂件名称创建流对象
        FileReader fr = new FileReader("read.txt");
// 定义变量，保存有效字符个数
        int len;
// 定义字符数组，作为装字符数据的容器
        char[] cbuf = new char[2];
// 循环读取
        while ((len = fr.read(cbuf))!=-1) {
            System.out.println(new String(cbuf,0,len));
        }
// 关闭资源
        fr.close();
    }
}
