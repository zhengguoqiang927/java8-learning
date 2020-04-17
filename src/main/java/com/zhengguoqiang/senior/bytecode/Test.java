package com.zhengguoqiang.senior.bytecode;

import javassist.*;

/**
 * @author zhengguoqiang
 */
public class Test {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zhengguoqiang.senior.bytecode.Hello");
        CtMethod say = ctClass.getDeclaredMethod("say");
        say.insertBefore("{System.out.println(\"Before..:\");}");
        say.insertAfter("{System.out.println(\"After..:\");}");
        Class aClass = ctClass.toClass();
        Hello hello = (Hello) aClass.newInstance();
        hello.say();
    }
}
