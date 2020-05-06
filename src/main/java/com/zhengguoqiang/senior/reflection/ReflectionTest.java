package com.zhengguoqiang.senior.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 * This program uses reflection to print all features of a class.
 */
public class ReflectionTest {
    public static void main(String[] args) {
        String name;
        if (args.length > 0){
            name = args[0];
        }else {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name (e.g. java.util.Date):");
            name = in.next();
        }

        try {
            Class<?> cl = Class.forName(name);
            Class<?> superclass = cl.getSuperclass();
            //Modifier.toString(cl.getModifiers()),通过位与运算返回Class/Member的修饰符
            //修饰符包括public、protected、private、abstract、static、interface等等
            String modifiers = Modifier.toString(cl.getModifiers());
            if (modifiers.length() > 0) {
                System.out.print(modifiers + " ");
            }
            System.out.print("class " + name);
            if (superclass != null && superclass != Object.class){
                System.out.print(" extends " + superclass.getName());
            }

            System.out.print("\n{\n");
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints all constructors of a class
     * @param cl
     */
    public static void printConstructors(Class cl){
        Constructor[] constructors = cl.getDeclaredConstructors();
        for (Constructor constructor:constructors){
            String name = constructor.getName();
            System.out.print("    ");
            String modifiers = Modifier.toString(constructor.getModifiers());
            if (modifiers.length() > 0){
                System.out.print(modifiers + " ");
            }
            System.out.print(name + "(");
            //print parameter types
            Class[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0;i< parameterTypes.length;i++){
                if (i > 0) System.out.print(", ");
                System.out.print(parameterTypes[i].getName());
            }
            System.out.println(");");
        }
    }

    /**
     * Prints all methods of a class
     * @param cl
     */
    public static void printMethods(Class cl){
        Method[] methods = cl.getDeclaredMethods();

        for (Method method:methods){
            Class<?> returnType = method.getReturnType();
            String name = method.getName();

            System.out.print("    ");
            //print modifiers,return type and method name
            String modifiers = Modifier.toString(method.getModifiers());
            if (modifiers.length()> 0) System.out.print(modifiers + " ");
            System.out.print(returnType.getName() + " " + name + "(");

            //print parameter types
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0;i< parameterTypes.length;i++){
                if (i > 0) System.out.print(", ");
                System.out.print(parameterTypes[i].getName());
            }
            System.out.println(");");
        }
    }

    /**
     * Prints all fields of a class
     * @param cl
     */
    public static void printFields(Class cl){
        Field[] fields = cl.getDeclaredFields();

        for (Field field:fields){
            Class<?> type = field.getType();
            String name = field.getName();
            System.out.print("    ");
            String modifiers = Modifier.toString(field.getModifiers());
            if (modifiers.length()>0) System.out.print(modifiers + " ");
            System.out.println(type.getName() + " " + name + ";");
        }
    }
}
