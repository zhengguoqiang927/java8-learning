package com.zhengguoqiang.collector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhengguoqiang
 */
public class CustomListCollectorTest {

    public static void main(String[] args) {
        String[] arrs = new String[]{"johnson","zheng","java 8","learning","com"};
        ToListCollector<String> collector = new ToListCollector<>();
        List<String> result = Arrays.stream(arrs)
                .parallel()
                .filter(s -> s.length() > 5)
                .collect(collector);
        System.out.println(result);
    }
}
