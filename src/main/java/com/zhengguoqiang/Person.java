package com.zhengguoqiang;

public class Person extends AbstractPerson {
    public Person() {
        System.out.println("Person...");
    }

    @Override
    public String getDescription() {
        return "Person is Object...";
    }

    public static void main(String[] args) {
        Person person = new Person();
        System.out.println(person.getDescription());
        Integer a = 100;
        Integer b = 100;
        System.out.println(a == b);//true
        a = 1000;
        b = 1000;
        System.out.println(a == b);//false
    }
}
