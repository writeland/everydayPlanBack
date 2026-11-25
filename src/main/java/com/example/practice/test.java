package com.example.practice;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class test {
    public static long getSum(int number) {
        long sum = 0;
        int i = 1;
        do{
            long result = 1l;
            for(int j=i; j>1; j--){
                result *= j;
            }
            i++;
            sum += result;
        }while(i<number);
        return sum;
    }
    // 取数字n项之和
    public static int getSum1(int n) {
        if(n == 1) {
            return 1;
        }
        return getSum1(n-1) + n;
    }
    // 取数字n的阶乘
    public static long getMuplti(int n) {
        if(n == 1) {
            return 1;
        }
        return getMuplti(n-1)*n;
    }
    // 输出平行四边形
    public static void printFour(int len) {
        int j = 1;
        while(j <= len) {
            for(int i = len-j; i>0; i--) {
                System.out.print(" ");
            }
            j++;
            System.out.println("******");
        }
    }
//    打印一个矩形
    public static void juZhen(int len, int width) {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < len; j++) {
                System.out.print("*");
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
//        int i = 1;
//        while(i <= 100) {
//            if(i % 7 == 0) {
//                System.out.println(i + "能被7整除");
//            }
//            i++;
////        }
//        long sum = getSum(10);
//        System.out.println("1~10的阶乘和为：" + sum);
//        Person p = new Person();
//        Person p2 = new Person();
//        System.out.println("名称" + p.name);
//        int n = 5;
//        printFour(10);
//        juZhen(10,2);
//        System.out.println(n + "阶乘为：" + getMuplti(n));
//        System.out.println("1~100的和为：" + getSum1(100));
//        String str = new String(new char[]{'b'});
        Calendar cal = Calendar.getInstance();
        System.out.println(cal);
        System.out.println(cal.getTime());
        Date date = cal.getTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));
    }
}
