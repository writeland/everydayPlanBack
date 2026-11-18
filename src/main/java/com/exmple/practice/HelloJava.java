package com.exmple.practice;
import com.example.daily.Entry;
import java.util.ArrayList;
import java.util.List;

public class HelloJava {
//    public static int doubleNumber(int number) {
//        return number * 2;
//    }
    public int doubleNumber(int number) {
        return number * 2;
    }
    public static String describe(Entry entry) {
        return "日程：" + entry.title;
    }
    public static void main(String[] args) {
//        Entry task = new Entry();
//        task.title = "学习 Java";
//        Entry anotherTask = task;
//        anotherTask.title = "学习 Spring Boot";
//        System.out.println(task.title);


//        int result = doubleNumber(5);
//        System.out.println(result);

//        HelloJava hello = new HelloJava();  // 创建对象
//        int result = hello.doubleNumber(5);  // 通过对象调用方法
//        System.out.println(result);
//        根据方法名前是否加上 static
//        静态方法属于类，可以通过 HelloJava.doubleNumber(5) 调用。
//        实例方法需要通过对象调用，比如 hello.doubleNumber(5)。
//        String description = describe(task);
//        System.out.println(description);
//        Task task1 = new Task("学习");
//        task1.setTitles("学习 Spring！");
//        System.out.println(task1.getTitles());
//
//        task1.setTitles("学习 Mybatis！");
////        System.out.println(task1.getTitles());
//
//        List<String> titles = new ArrayList<>();
//        titles.add("学习 Java");
//        titles.add("学习 Spring");
//        titles.add("学习 MyBatis");
//
//        System.out.println(titles.get(1));
//        System.out.println(titles.size());


//        List<Entry> entries = new ArrayList<>();
//        Entry first = new Entry();
//        first.title = "学习 Java";
//        entries.add(first);
//
//        Entry second = new Entry();
//        second.title = "学习 Spring";
//        entries.add(second);
//
//        for (Entry entry : entries) {
//            System.out.println(entry.title);
//        }

//        Task task = new Task("学习 Java");
//
//        try {
//            task.setTitle("");
//            System.out.println("设置成功");
//        } catch (IllegalArgumentException e) {
//            System.out.println("设置失败：" + e.getMessage());
//        }
//
//        System.out.println("当前标题：" + task.getTitle());
        List<Task> tasks = List.of(
                new Task("学习 Java"),
                new Task("运动"),
                new Task("学习 MySQL")
        );

        List<String> studyTitles = tasks.stream()
                .filter(task -> task.getTitle().contains("学习"))
                .map(task -> task.getTitle())
                .toList();

        System.out.println(studyTitles);
    }
}
