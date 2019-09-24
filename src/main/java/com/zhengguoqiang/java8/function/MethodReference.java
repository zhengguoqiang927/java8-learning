package com.zhengguoqiang.java8.function;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author zhengguoqiang
 */
public class MethodReference {

    public static void main(String[] args) {
        //1.静态方法
        Function<String, Integer> function = Integer::parseInt;
        int result = function.apply("123");
        System.out.println(result);

        //2.实例方法，需要提供初始值
        BiFunction<String, Integer, Character> biFunction = String::charAt;
        Character character = biFunction.apply("hello zhengguoqiang",3);
        System.out.println(character);

        //3.通过已实例化的对象直接进行方法引用
        String string = "hello";
        Function<Integer, Character> characterFunction = string::charAt;
        Character character1 = characterFunction.apply(3);
        System.out.println(character1);

        BiFunction<String,Integer,Apple> appleBiFunction = Apple::new;
        Apple red = appleBiFunction.apply("red", 100);
        System.out.println(red);

        ThreeFunction<String,String,Integer,ComplexApple> threeFunction = ComplexApple::new;
        ComplexApple complexApple = threeFunction.apply("HongFuShi", "red", 100);
        System.out.println(complexApple);
    }
}
