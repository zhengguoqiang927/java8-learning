package com.zhengguoqiang.senior.bytecode;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.bytecode.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zhengguoqiang
 */
public class JavassistClass {

    @Test
    public void createClass() throws CannotCompileException {
        //ClassFile：用于创建一个新的类文件
        //FieldInfo：用于向类中添加一个新的字段
        ClassFile cf = new ClassFile(false,
                "com.zhengguoqiang.senior.bytecode.JavassistGeneratedClass", null);
        cf.setInterfaces(new String[]{"java.lang.Cloneable"});

        FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
        f.setAccessFlags(AccessFlag.PUBLIC);
        cf.addField(f);

        ClassPool classPool = ClassPool.getDefault();
        Field[] fields = classPool.makeClass(cf).toClass().getFields();
        assertEquals(fields[0].getName(), "id");
    }

    @Test
    public void loadByteCode() throws NotFoundException, BadBytecode {
        ClassPool classPool = ClassPool.getDefault();
        ClassFile cf = classPool.get("com.zhengguoqiang.senior.bytecode.Point").getClassFile();
        MethodInfo minfo = cf.getMethod("move");
        CodeAttribute ca = minfo.getCodeAttribute();
        CodeIterator ci = ca.iterator();

        List<String> operations = new LinkedList<>();
        while (ci.hasNext()) {
            int index = ci.next();
            int op = ci.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }

        assertEquals(operations, Arrays.asList(
                "aload_0",
                "iload_1",
                "putfield",
                "aload_0",
                "iload_2",
                "putfield",
                "return"
        ));
    }

    @Test
    public void addFieldToClass() throws NotFoundException, CannotCompileException {
        ClassFile cf = ClassPool.getDefault().get("com.zhengguoqiang.senior.bytecode.Point").getClassFile();

        FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
        f.setAccessFlags(AccessFlag.PUBLIC);
        cf.addField(f);

        ClassPool classPool = ClassPool.getDefault();
        Field[] fields = classPool.makeClass(cf).toClass().getFields();
        List<String> fieldList = Stream.of(fields).map(Field::getName).collect(Collectors.toList());
        assertTrue(fieldList.contains("id"));
    }

    @Test
    public void addConstructorToClass() throws NotFoundException, BadBytecode, DuplicateMemberException {
        ClassFile cf = ClassPool.getDefault().get("com.zhengguoqiang.senior.bytecode.Point").getClassFile();
        Bytecode code = new Bytecode(cf.getConstPool());
        code.addAload(0);
        code.addInvokespecial("java/lang/Object", MethodInfo.nameInit, "()V");
        code.addReturn(null);

        MethodInfo minfo = new MethodInfo(cf.getConstPool(), MethodInfo.nameInit, "()V");
        minfo.setCodeAttribute(code.toCodeAttribute());
        cf.addMethod(minfo);

        CodeIterator ci = code.toCodeAttribute().iterator();
        List<String> operations = new LinkedList<>();
        while (ci.hasNext()) {
            int index = ci.next();
            int op = ci.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }

        assertEquals(operations, Arrays.asList("aload_0", "invokespecial", "return"));
    }
}
