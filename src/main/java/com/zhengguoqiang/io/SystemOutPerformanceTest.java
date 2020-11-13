package com.zhengguoqiang.io;

import java.io.*;

public class SystemOutPerformanceTest {

    private static final String line1 = "[stdout] very very long line very very long line very very long line very very long line very very long line very very long line very very long line very very long line";
    private static final String line2 = "[file] very very long line very very long line very very long line very very long line very very long line very very long line very very long line very very long line\n";
    private static final String line3 = "[/dev/stdout] very very long line very very long line very very long line very very long line very very long line very very long line very very long line very very long line\n";


    public static void main(String[] args) throws IOException {
        int count = Integer.parseInt(args[0]);

        long t1 = stdout(count);

        File tempFile = File.createTempFile("test", "log");
        tempFile.deleteOnExit();
        long t2 = file(tempFile, line2, count);

        File f2 = new File("/dev/stdout");
        long t3 = file(f2,line3,count);

        System.out.println("lines: " + String.format("%,d", count));
        System.out.println("System.out.println: " + String.format("%,d", t1) + " ms");
        System.out.println("file: " + String.format("%,d", t2) + " ms");
        System.out.println("/dev/stdout: " + String.format("%,d", t3) + " ms");
        System.out.println("text length:" + line1.getBytes().length);
    }

    private static long stdout(int count) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            System.out.println(line1);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static long file(File file,String line,int count) throws IOException {
        byte[] bytes = line.getBytes();
        try (
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos,1024 * 4);
        ){
            long start = System.currentTimeMillis();
            for (int i = 0;i<count;i++){
                bos.write(bytes);
            }
            bos.flush();
            long end = System.currentTimeMillis();
            return end - start;
        }
    }
}
