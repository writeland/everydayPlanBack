package com.example.javaTest;

import java.util.ArrayList;
import java.util.List;

public class ListDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add(1,"insert");
        System.out.println("List为: " + list);
        String str = list.get(1);
        System.out.println("获取的元素: " + str);
        list.set(1,"change");
        System.out.println("更改后List为: " + list);
        list.remove(1);
        System.out.println("删除后List为: " + list);
    }
}
