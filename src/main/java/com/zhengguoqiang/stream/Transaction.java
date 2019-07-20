package com.zhengguoqiang.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
@Data
@AllArgsConstructor
public class Transaction {

    private final Trader trader;
    private final int year;
    private final int value;

    @Data
    @AllArgsConstructor
    public static class Trader {
        private final String name;
        private final String city;
    }

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul","Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian,2011,300),
                new Transaction(raoul,2012,1000),
                new Transaction(raoul,2011,400),
                new Transaction(mario,2012,710),
                new Transaction(mario,2012,700),
                new Transaction(alan,2012,950)
        );

        //1.“找出2011年的所有交易并按交易额排序（从低到高)”
        System.out.println("=============Problem One=============");
        transactions.stream().filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue).reversed())
                .forEach(System.out::println);

        //2.交易员都在哪些不同的城市工作过
        System.out.println("=============Problem Two=============");
        transactions.stream().map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);

        //3.查找所有来自剑桥的交易员，并按姓名排序
        System.out.println("=============Problem Three=============");
        transactions.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .sorted(Comparator.comparing(transaction -> transaction.getTrader().getName()))
                .forEach(System.out::println);

        //4.返回所有交易员的姓名字符串，按字母顺序排序
        System.out.println("=============Problem Four=============");
        String reduce = transactions.stream().map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (s, s2) -> s + "," + s2);
        String reduce1 = transactions.stream().map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining(","));
        System.out.println(reduce1);

        //5.有没有交易员是在米兰工作的
        System.out.println("=============Problem Five=============");
        boolean b = transactions.stream()
                .anyMatch(transaction ->  "Milan".equals(transaction.getTrader().getCity()));
        System.out.println(b);

        //6.打印生活在剑桥的交易员的所有交易额
        System.out.println("=============Problem Six=============");
        transactions.stream().filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        //7.所有交易中，最高的交易额是多少
        System.out.println("=============Problem Seven=============");
        transactions.stream().mapToInt(Transaction::getValue).max().ifPresent(System.out::println);
        transactions.stream().map(Transaction::getValue).reduce(Integer::max).ifPresent(System.out::println);

        //8.所有交易中，最小的交易额是多少
        System.out.println("=============Problem Eight=============");
        transactions.stream().mapToInt(Transaction::getValue).min().ifPresent(System.out::println);
        transactions.stream().mapToInt(Transaction::getValue).reduce(Integer::min).ifPresent(System.out::println);
    }
}
