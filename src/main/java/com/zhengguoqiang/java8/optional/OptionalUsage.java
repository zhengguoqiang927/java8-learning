package com.zhengguoqiang.java8.optional;

import java.util.Optional;

/**
 * @author zhengguoqiang
 */
public class OptionalUsage {


    public static void main(String[] args) throws Exception {
//        Optional<Insurance> optional = Optional.ofNullable(null);
//        optional.orElse(new Insurance());
//        optional.orElseGet(Insurance::new);
//        optional.orElseThrow(Exception::new);
//        optional.orElseThrow(() -> new RuntimeException("not have reference"));
//
//        String insuranceName = getInsuranceName(null);
//        System.out.println(insuranceName);
        Person person = new Person();
        String insuranceNameByOptional = getInsuranceNameByOptional(person);
        System.out.println(insuranceNameByOptional);
    }


    private static String getInsuranceName(Insurance insurance) {
        return Optional.ofNullable(insurance).map(Insurance::getName).orElse("unknown");
    }

    private static String getInsuranceNameByOptional(Person person) {
        return Optional.ofNullable(person)
                .map(Person::getCar)
                .map(Car::getInsurance)
                .map(Insurance::getName)
                .orElse("unknown");
    }
}
