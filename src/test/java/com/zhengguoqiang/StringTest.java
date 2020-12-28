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
import java.util.stream.Stream;

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
        String t1 = "B10,0.8,0.74,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1,0,1,0,0,1,1,1,1";
        String[] split = t1.split(",");
//        Arrays.stream(split).skip(3).map(Byte::valueOf);
        long count = Arrays.stream(split).skip(3).filter("0"::equals).count();
        System.out.println(2 << 128);

        Map<String,Long> map = new HashMap<>();
        map.put("1",1L);
        map.put("2",2L);
        map.put("3",3L);
        map.put("4",4L);
        map.entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).limit(6).forEach(entry -> {
            System.out.println(entry.getKey() + "," + entry.getValue());
        });
    }

    @Test
    public void format(){
        String s = "3BA71E99560A,0.28696194635059263,0.0,0.0,0.013345605154164749,9.125336496783319E-5,0.4632595168374817,0.4531257284042702,0.0,0.0,0.0,0.03,0.415041782729805,0.11,0.5,0.2,0.0010722507805354946,0.8,0.2,0.0,0.0,0.63,0.63,0.01440922190201729,0.17,0.0,0.0,0.40625000000000006,0.17,0.5,0.14,0.0,9.810241892535807E-4,0.0,0.0,6.44620640752917E-5,0.0,0.09,0.18181818181818182,0.5,0.0,0.49,0.17,0.0,0.17,0.0,0.31,0.0,0.0,0.0,0.06,0.03,0.37,0.97,0.0,2.499993750015625E-6,2.499993750015625E-6,9.99999000001E-7,5.555246930726071E-5,1.0E-7,1.4283673760891302E-4,9.81815310668892E-10,1.9956096587507485E-4,2.499993750015625E-6,1.4704150290531953E-7,2.499993750015625E-6,8.18181818129752E-11,0.003407979590390476,5.235759958123636E-4,5.024987437531407E-4,2.2511249718609378E-4,8.18181818129752E-11,1.0E-5,9.090900826453794E-7,5.727270910625089E-10,0.0012689898480812154,9.212670965913117E-4,0.0017959049119079889,0.0,2.2311468094600624E-4,0.0,0.001665972511453561,0.010569490455907871,0.0,0.0013235172470841261,0.0,0.0,0.002464673020046007,0.004987531172069825,0.0,0.010378305992634751,0.0,0.006720430107526882,0.45,0.55,0.004178272980501393,0.00966183574879227,1.0,1.0,0.0,0.006701414743112435,0.0,0.0,0.004319744658807015,0.0,0.0,0.005168389463154384,1.0,0.008001,0.01981960044939056,0.0,0.42,0.81,0.84,0.0,0.0,0.0013105436435903617,0.999959999799999,0.0,0.0,0.0,0.26657281248835674,0.0,0.0,0.013275980823816631,0.004378531073446328,0.0,0.048367383673836736,0.97,0.0,1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0";
        Arrays.stream(s.split(",")).skip(1).forEach(System.out::println);
    }

}
