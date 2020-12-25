package com.zhengguoqiang;

import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class StringTest {
    private static final String a = "123";
    private static final String b = "123";

    @Test
    public void test(){
        System.out.println(a == b);
        System.out.println(a.equals(b));
        int[] array = new int[]{};

        String s = "Aman";
        System.out.println(s.hashCode());
    }

    @Test
    public void binary(){
        System.out.println(Integer.MAX_VALUE);
        int integer = (1 << 29) - 1;
        System.out.println(Integer.toBinaryString(integer));
    }

    @Test
    public void forreturn(){
        HashSet<String> workers = new HashSet<>();
        workers.add("111");
        System.out.println(workers.size());
    }

    @Test
    public void compute(){
        String A2 = "A2,0.51,0.33,1,1";
        String B10 = "B10,0.8,0.74,1,1";
        String[] a2 = A2.split(",");
        String[] b10 = B10.split(",");
        System.out.println(a2.length);
        double fenzi = 0,fenmuA = 0,fenmuB = 0;
        for (int i = 1;i<a2.length;i++){
            double a = Double.parseDouble(a2[i]);
            double b = Double.parseDouble(b10[i]);
            fenzi +=  a * b;
            fenmuA += a * a;
            fenmuB += b * b;
        }
        double result = fenzi / (Math.sqrt(fenmuA) * Math.sqrt(fenmuB)) ;
    }

    List<String> seedList = new ArrayList<>(10000);

    ExecutorService pool = Executors.newFixedThreadPool(4);

    PriorityBlockingQueue<TargetObject> queue = new PriorityBlockingQueue<>(100);

    class TargetObject implements Comparable<TargetObject>{
        private String seed;
        private String target;
        private double score;

        public TargetObject(String seed, String target, double score) {
            this.seed = seed;
            this.target = target;
            this.score = score;
        }

        @Override
        public int compareTo(TargetObject o) {
            return this.score > o.score ? 1 : -1;
        }
    }

    class ComputeTask implements Runnable {

        private final String targetTxt;

        public ComputeTask(String targetTxt) {
            this.targetTxt = targetTxt;
        }

        @Override
        public void run() {
            for (String seed : seedList){
                queue.put(compute(seed,targetTxt));
                if (queue.size() > 50) {
                    queue.poll();
                }
            }
        }
    }

    private TargetObject compute(String seedTxt,String targetTxt){
        String[] a2 = seedTxt.split(",");
        String[] b10 = targetTxt.split(",");
        double fenzi = 0,fenmuA = 0,fenmuB = 0;
        for (int i = 1;i<a2.length;i++){
            double a = Double.parseDouble(a2[i]);
            double b = Double.parseDouble(b10[i]);
            fenzi +=  a * b;
            fenmuA += a * a;
            fenmuB += b * b;
        }
        return new TargetObject(a2[0],b10[0],fenzi / (Math.sqrt(fenmuA) * Math.sqrt(fenmuB)));
    }


    @Test
    public void process(){

        List<CompletableFuture<Void>> futureList = new ArrayList<>();

        String seedFile = "/Users/zhengguoqiang/Downloads/java/input/seed.txt";
        String allFile = "/Users/zhengguoqiang/Downloads/java/input/all.txt";
        try(BufferedReader bisSeed = new BufferedReader(new FileReader(seedFile));
            BufferedReader bisAll = new BufferedReader(new FileReader(allFile))){
            String s = null;
            while ((s = bisSeed.readLine()) != null){
                seedList.add(s);
            }
            while ((s = bisAll.readLine()) != null){
//                pool.submit(new ComputeTask(s));
                futureList.add(CompletableFuture.runAsync(new ComputeTask(s)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        List<String> result = new ArrayList<>();
        TargetObject targetObject = null;
        while ( (targetObject = queue.poll()) != null){
            result.add(targetObject.target);
            System.out.println(targetObject.target + "," + targetObject.score) ;
        }
        Collections.reverse(result);
        result.forEach(System.out::println);
    }

    @Test
    public void transfer(){
        String s = "8.212802847104987E-4";
        double v = Double.parseDouble(s);
        System.out.println(v);

        double d1 = v;
        String s1 = "0.28696194635059263";
        double d2 = 0.35892701185277603;
        String s2 = "0.35892701185277603";
        System.out.println(d1 * d2);
        BigDecimal bigDecimal1 = new BigDecimal(s);
        BigDecimal bigDecimal2 = new BigDecimal(s2);
        BigDecimal multiply = bigDecimal1.multiply(bigDecimal2);
        System.out.println(multiply.setScale(8,BigDecimal.ROUND_HALF_UP).doubleValue());

    }

    @Test
    public void bitset(){
        String seed = "A2,0.51,0.33,1,1";
        String all = "B10,0.8,0.74,1,1";
        boolean b = seed.substring(seed.length() - 3).equals(all.substring(all.length() - 3));
        System.out.println(b);
    }


}
