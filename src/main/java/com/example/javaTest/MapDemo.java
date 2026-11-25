package com.example.javaTest;

import java.util.*;

public class MapDemo {
    public static void main(String[] args) {
        /*
        put(key,value):往map集合中添加键值对
        get(key):根据key，获取value;
        remove(key):根据key,删除对应的键值对
        keySet:key的集合
        */
//        Map<String,String> map = new HashMap<>();
//        String s1 = map.put("A", "aaa");//向Map集合中添加键值对
//        System.out.println("s1:"+s1);
//        System.out.println(map);
//        map.put("B","bbb");
//        map.put("C","ccc");
//        System.out.println(map);
//        String s2 = map.put("C", "vvvv");//如果有是重复的key,那么会覆盖原来的value,同时会返回被覆盖的value值
//        System.out.println("s2:"+s2);
//        System.out.println(map);
//        System.out.println("---------------");
//        String value = map.get("C");//获取指定的key 对应的value
//        System.out.println("value---->"+value);
//        System.out.println("------------");
//        System.out.println("size:"+map.size());
//        map.remove("C");
//        System.out.println(map);
//        //遍历map的key,然后根据key取相应的值,完成遍历
//        Set<String> keySet = map.keySet();
//        System.out.println("key值的参数表为：" + keySet);
//        for(String key:keySet){
//            System.out.println(key+"------->"+map.get(key));
//        }
//
//        Map<String,String> map1 = new TreeMap<>();
//        map1.put("A1","bbb");
//        map1.put("A0", "aaa");
//        map1.put("C","ccc");
//        System.out.println(map1);

        List<String> list1 = new ArrayList<>();
        //向指定的集合中添加元素
        Collections.addAll(list1,"a","bb","ccc","ddd","000");
//        自己写一个 Comparator 比较规则，就可以把它传给 list1.sort()。
//        比如，让字符串按长度从小到大排序
        list1.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });
        System.out.println(list1);

        //找到指定的元素的下标
        int index = Collections.binarySearch(list1,"bb");
        System.out.println("index:"+index);
        Collections.reverse(list1);//反转指定列表中元素的顺序。
        System.out.println(list1);
        //使⽤默认的随机源随机排列指定的列表。
        Collections.shuffle(list1);
        System.out.println(list1);
        //排序
        Collections.sort(list1);
        System.out.println(list1);
    }
}
