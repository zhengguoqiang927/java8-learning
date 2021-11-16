package com.zhengguoqiang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

public class Solution {
    //{"key":"A1","value":[0.5,0,3,1,1]}
    Map<String, List<Double>> seedMap = new HashMap<>();

    //{"B1":"B1,0.5,0.3,0,1"}
    ConcurrentHashMap<String, List<Double>> allS = new ConcurrentHashMap<>();

    //{"key":"A1","value":["B1","B2","B1"]}
    Map<String, List<String>> allMap;

    List<String> seedList;

    PriorityBlockingQueue<TargetObject> queue;

    int thresold;

    int startIndex = 129;

    int maxLimit = 50;

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


    class Task implements Runnable {
        private String key;
        private List<String> targets;

        public Task(String key, List<String> targets) {
            this.key = key;
            this.targets = targets;
        }

        @Override
        public void run() {
            compute(key, targets);
        }
    }

    //allMap:{"key":"A1","value":["B1","B2","B1","B2"]}
    private void compute(String key, List<String> targets) {
        if (targets == null || targets.size() == 0) return;
        Map<String, Long> collect = targets.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        List<String> targetKeys = collect.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()).limit(maxLimit).map(Map.Entry::getKey).collect(Collectors.toList());
        List<Double> seedValue = seedMap.get(key);

        for (String target : targetKeys) {
            TargetObject compute = compute(key, target, seedValue, allS.get(target));
            if (compute == null) continue;
            queue.add(compute);
            if (queue.size() > thresold) {
                queue.poll();
            }
        }

    }

    private TargetObject compute(String seedKey, String targetKey, List<Double> seeds, List<Double> targets) {
        double fenzi = 0, fenmuA = 0, fenmuB = 0;
        for (int i = 0; i < seeds.size(); i++) {
            double a = seeds.get(i);
            double b = targets.get(i);
            if (a == 0 && b == 0) continue;
            fenzi += a * b;
            fenmuA += Math.pow(a, 2);
            fenmuB += Math.pow(b, 2);
        }
        if (fenzi == 0 || fenmuA == 0 || fenmuB == 0) {
            return null;
        }
        BigDecimal f = new BigDecimal(fenzi);
        double t = Math.sqrt(fenmuA) * Math.sqrt(fenmuB);
        BigDecimal m = new BigDecimal(t);
        return new TargetObject(seedKey, targetKey, f.divide(m, 8, BigDecimal.ROUND_HALF_UP).doubleValue());
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
        List<TargetObject> result = new ArrayList<>(outputCount);

        seedList = Files.readAllLines(Paths.get(seedFile));
        queue = new PriorityBlockingQueue<>(outputCount);
        thresold = outputCount;

        //{"key":"A1","value":[0.5,0,3,1,1]}
        seedMap = new HashMap<>(seedList.size());

        //{"B1":[B1,0.5,0.3,0,1]}
        allS = new ConcurrentHashMap<>(outputCount);
        //{"key":"A1","value":["B1","B2","B1"]}
        allMap = new HashMap<>();

        //a[0]=[B1,B2,B3],a[1] = [B1,B2]
        List<Set<String>> seedArray = new ArrayList<>(128);
        for (int i = 0; i < 128; i++) {
            seedArray.add(i, new HashSet<>());
        }

        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        try (BufferedReader bisAll = new BufferedReader(new InputStreamReader(new FileInputStream(allFile)), 10 * 1024 * 1024)) {
            String s = null;
            while ((s = bisAll.readLine()) != null) {
                String[] split = s.split(",");
                String id = split[0];
                List<Double> targets = new ArrayList<>(256);
                for (int i = 1; i < split.length; i++) {
                    targets.add(Double.parseDouble(split[i]));
                    if (i >= startIndex && Double.parseDouble(split[i]) == 1) {
                        seedArray.get(i - startIndex).add(id);
                    }
                }
                allS.put(id, targets);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String seed : seedList) {
            String[] split = seed.split(",");
            String id = split[0];
            List<Double> doubles = new ArrayList<>(256);
            for (int i = 1; i < split.length; i++) {
                doubles.add(Double.parseDouble(split[i]));
                if (i >= startIndex && Double.parseDouble(split[i]) == 1) {
                    if (allMap.containsKey(id)) {
                        allMap.get(id).addAll(seedArray.get(i - startIndex));
                    } else {
                        allMap.put(id, new ArrayList<>(seedArray.get(i - startIndex)));
                    }
                }
            }
            seedMap.put(id, doubles);
            futureList.add(CompletableFuture.runAsync(new Task(id, allMap.get(id))));
//                if (allMap.size() > 10000) allMap.clear();
        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        TargetObject object = null;
        while ((object = queue.poll()) != null) {
            result.add(object);
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
        long start = System.currentTimeMillis();
        solution.process(seedFile, allFile, 1000, null);
        System.out.println("time:" + (System.currentTimeMillis() - start));

//        String seed = "A1,0.52,0.1,1,0";
//        String[] all = {"B1,0.74,0.83,1,0","B2,0.8,0.29,0,0","B3,0.79,0.81,1,0"};
//        Solution solution = new Solution();
//        for (String target:all){
//            System.out.println(solution.compute(seed,target));
//        }

    }
}
