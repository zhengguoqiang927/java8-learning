package com.zhengguoqiang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;

public class Solution {
    //{"key":"1,1","value":["B1,0.5,0,3,1,1"]}
    Map<String,List<String>> seedMap = new HashMap<>();

    //{"key":"1,1","value":["B1","B2"]}
    Map<String,List<String>> allMap = new HashMap<>();
    //{"B1":"B1,0.5,0.3,0,1"}
    Map<String,String> allS = new HashMap<>();

    List<String> seedList;

    PriorityBlockingQueue<TargetObject> queue;

    int thresold;

    int lastIndex = 129;

    class TargetObject implements Comparable<TargetObject> {
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

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object anObject) {
            if (this == anObject) {
                return true;
            }
            if (anObject instanceof TargetObject) {
                TargetObject obj = (TargetObject) anObject;
                return this.target.equals(obj.target)
                        && this.score == obj.score;
            }

            return false;
        }

        @Override
        public String toString() {
            return "[" + seed + "," + target + "," + score + "]";
        }
    }



    class ComputeTask implements Runnable {

        @Override
        public void run() {
            if (allMap.size() > 0){
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                allMap.forEach((s, targets) -> {
                    if (seedMap.containsKey(s)){
                        futures.add(CompletableFuture.runAsync(new Task(s,targets)));
                    }
                });
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        }
    }

    class Task implements Runnable{
        private String key;
        private List<String> targets;

        public Task(String key, List<String> targets) {
            this.key = key;
            this.targets = targets;
        }

        @Override
        public void run() {
            compute(key,targets);
        }
    }



    //allMap:{"key":"1,1","value":[B1,B2]}
    private void compute(String key,List<String> targets){
        for (String target : targets) {
            TargetObject max = null;
            for (String source:seedMap.get(key)){
                TargetObject compute = compute(source, allS.get(target));
                if (compute == null) continue;
                max = max == null ? compute : (compute.score > max.score ? compute : max);
            }
            if (max == null) continue;
            queue.add(max);
            if (queue.size() > thresold){
                queue.poll();
            }
        }
    }


    private TargetObject compute(String seedTxt, String targetTxt) {
        String[] a2 = seedTxt.split(",");
        String[] b10 = targetTxt.split(",");
        double fenzi = 0, fenmuA = 0, fenmuB = 0;
        for (int i = 1; i < a2.length; i++) {
            double a = Double.parseDouble(a2[i]);
            double b = Double.parseDouble(b10[i]);
            fenzi += a * b;
            fenmuA += Math.pow(a, 2);
            fenmuB += Math.pow(b, 2);
        }
        if (fenzi == 0 || fenmuA == 0 || fenmuB == 0) {
            return null;
        }
        BigDecimal f = new BigDecimal(fenzi);
        double sqrt = Math.sqrt(fenmuA) * Math.sqrt(fenmuB);
        BigDecimal m = new BigDecimal(sqrt);
        return new TargetObject(a2[0], b10[0], f.divide(m, 8, BigDecimal.ROUND_HALF_UP).doubleValue());
    }


    /**
     * 主体逻辑实现demo，实现代码时请注意逻辑严谨性，涉及到操作文件时，保证文件有开有闭等。
     *
     * @param seedFile    种子集文件
     * @param allFile     候选集文件
     * @param outputCount 需要输出的结果数量
     * @param tempDir     临时文件存放目录
     */
    public void process(String seedFile, String allFile, int outputCount, String tempDir) throws Exception {
        List<String> result = new ArrayList<>(outputCount);

        seedList = Files.readAllLines(Paths.get(seedFile));
        queue = new PriorityBlockingQueue<>(outputCount);
        thresold = outputCount;

        //{"key":"1,1","value":["A1,0.5,0,3,1,1"]}
        seedMap = new HashMap<>(seedList.size());
        //{"key":"1,1","value":["B1","B2"]}
        allMap = new HashMap<>(outputCount);
        //{"B1":"B1,0.5,0.3,0,1"}
        allS = new HashMap<>(outputCount);

        CompletableFuture.runAsync(() -> {
            try (BufferedReader bisAll = new BufferedReader(new InputStreamReader(new FileInputStream(allFile)), 10 * 1024 * 1024)) {
                String s = null;
                while ((s = bisAll.readLine()) != null) {
                    String[] split = s.split(",");
                    allS.put(split[0], s);
                    String a255 = Arrays.stream(split).skip(lastIndex).filter("0.0"::equals).count() + "";
//                            s.substring(s.length() - lastIndex);
                    if (allMap.containsKey(a255)) {
                        allMap.get(a255).add(split[0]);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(split[0]);
                        allMap.put(a255, list);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).runAfterBoth(CompletableFuture.runAsync(() -> {
            for (String seed:seedList){
                String count = Arrays.stream(seed.split(",")).skip(lastIndex).filter("0.0"::equals).count() + "";
//                String s255 = seed.substring(seed.length() - lastIndex);
                if (seedMap.containsKey(count)){
                    seedMap.get(count).add(seed);
                }else{
                    List<String> tmp = new ArrayList<>();
                    tmp.add(seed);
                    seedMap.put(count,tmp);
                }
            }
        }),new ComputeTask()).join();



//        List<CompletableFuture<Void>> futureList = new ArrayList<>();



//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        TargetObject object = null;
        while ((object = queue.poll()) != null) {
            result.add(object.target);
        }

        Collections.reverse(result);
        result.forEach(targetObject -> System.out.println(targetObject.toString()));
        //请通过此方法输出答案,多次调用会追加记录
//        MainFrame.addSet(result);
    }

    public static void main(String[] args) throws Exception {
        String seedFile = "/Users/zhengguoqiang/Downloads/java/input/seed.txt";
        String allFile = "/Users/zhengguoqiang/Downloads/java/input/all.txt";
        Solution solution = new Solution();
        solution.process(seedFile, allFile, 7, null);


//        String seed = "A1,0.52,0.1,1,0";
//        String[] all = {"B1,0.74,0.83,1,0","B2,0.8,0.29,0,0","B3,0.79,0.81,1,0"};
//        Solution solution = new Solution();
//        for (String target:all){
//            System.out.println(solution.compute(seed,target));
//        }

    }
}
