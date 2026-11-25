package com.example.practice;

import java.util.Objects;

public class Person {
    String name;
    int age;
    static {
        System.out.println("这是静态代码块~");
    }
    {
        System.out.println("这是构造代码块~");
    }

    Person(){
        System.out.println("执行构造函数");
        name = "张三";
        age = 18;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    public void eat() {
        System.out.println("吃法");
    }

    public void sleep() {
        System.out.println("睡觉");
    }
}
