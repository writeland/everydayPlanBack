package com.example.javaTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionTest {
    public static void main(String[] args) {
//        Collection c1 = new ArrayList();
//
//        boolean flag = c1.add("123");
//        c1.add(true);
//        c1.add(1.231);
//        Collection c2 = new ArrayList();
//        c2.add(12);
//        c2.add(45);
//        c2.add(67);
//
//        c1.addAll(c2);
//
//        c1.remove("123");
//        System.out.println(c1);
        Collection<String> c1 = new ArrayList<>();
        c1.add("aa");
        c1.add("bb");
        c1.add("cc");
        c1.add("dd");
        Iterator<String> iterator = c1.iterator();
        if(iterator.hasNext()){
            System.out.println(iterator.next());
            //不能使⽤collection删除⽅法，因为遍历同时不能使⽤集合删除⽅法，否则出现并发修改异常。
            //可以使⽤迭代器的删除⽅法
            //c1.remove(s);
            //iterator.remove();
        }
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
    }
}
