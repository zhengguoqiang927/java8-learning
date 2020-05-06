package com.zhengguoqiang;

public abstract class AbstractPerson {

    private String name;

    public AbstractPerson() {
        System.out.println("AbstractPerson...");
    }

    public abstract String getDescription();

    public String getName() {
        return name;
    }
}
