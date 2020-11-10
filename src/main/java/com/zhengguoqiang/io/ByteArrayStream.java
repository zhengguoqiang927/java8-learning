package com.zhengguoqiang.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Set;

public class ByteArrayStream {
    public static void main(String[] args) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write("This text is converted to bytes".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = outputStream.toByteArray();
            for (byte b:bytes){
                System.out.println(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Reader reader = new InputStreamReader(new FileInputStream(""),"UTF-8");
            Writer writer = new OutputStreamWriter(new FileOutputStream("xxx"));
            Reader reader1 = new BufferedReader(new FileReader(new File("")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bufferedInputStream(){
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream("xxx.txt"),1024 * 1024);
            inputStream.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void power(){
        double pow = Math.pow(2, 10);
        System.out.println(pow);
    }

    @Test
    public void linkedhashmap(){
        LinkedHashMap<Integer,String> linkedHashMap = new LinkedHashMap<>(5,0.75f,true);
        linkedHashMap.put(1,null);
        linkedHashMap.put(2,null);
        linkedHashMap.put(3,null);
        linkedHashMap.put(4,null);
        linkedHashMap.put(5,null);

        linkedHashMap.get(8);
        linkedHashMap.remove(1);

        Set<Integer> keySet = linkedHashMap.keySet();
        Integer[] integers = keySet.toArray(new Integer[0]);

//        Assert.assertEquals("[2,3,4,5,1]",integers.toString());
    }
}
