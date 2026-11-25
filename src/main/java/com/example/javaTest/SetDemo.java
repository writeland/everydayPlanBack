package com.example.javaTest;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class SetDemo {
    public static class Person implements Comparable<Person> {
        private String name;
        private int age;

        @Override
        public int compareTo(Person o) {
            if (age > o.getAge()) {
                return 1;
            } else if (age < o.getAge()) {
                return -1;
            } else {
                return name.compareTo(o.getName());
            }
//return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age &&
                    name.equals(person.name);
        }

//      重写 equals() 时，也要重写 hashCode()，保证 equals() 相等的对象有相同的哈希值。
        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
//        Set<String> set1 = new HashSet();
//        set1.add("aaa");
//        set1.add("bbb");
//        set1.add("ccc");
//        set1.add("ddd");
//        System.out.println(set1);
//        boolean flag = set1.add("aaa");//添加的时候，判断是否重复
//        System.out.println("flag:"+flag);
//        System.out.println(set1);
//        System.out.println("-------------------");
//        Set<Integer> set2 = new HashSet<>();
//        set2.add(1);
//        set2.add(2);
//        set2.add(3);
//        set2.add(2);
//        System.out.println(set2);
//        Person p1 =new Person("张三",23);
//        Person p2 = new Person("lisi",56);
//        Person p3 = new Person("张三",23);
//        Set<Person> set3 = new HashSet<>();
//        set3.add(p1);
//        set3.add(p2);
//        set3.add(p3);
//        System.out.println(set3);


//
//        Set<String> set1 =new TreeSet<>();
//        set1.add("aaa");
//        set1.add("ccc");
//        set1.add("ddd");
//        set1.add("bbb");
//        System.out.println(set1);
//        boolean flag = set1.add("aaa");
//        System.out.println("flag:"+flag);
//        System.out.println(set1);
//        Person p1 =new Person("张三",23);
//        Person p2 = new Person("lisi",56);
//        Person p3 = new Person("wowo",23);
//        Set<Person> set2 =new TreeSet<>();
//        set2.add(p1);
//        set2.add(p2);
//        set2.add(p3);
//        System.out.println(set2);

        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(3);
        treeSet.add(1);
        treeSet.add(2);

        System.out.println(treeSet); // [1, 2, 3]
    }
}