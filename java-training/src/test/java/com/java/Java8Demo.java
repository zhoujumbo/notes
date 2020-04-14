package com.java;

import com.fortunetree.basic.support.commons.business.jackson.JacksonUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Java8Demo {

    private  List<Person> persons = Arrays.asList(new Person("Joe", 12),
            new Person("Jim", 19), new Person("John", 21));
//    public static void main(String[] args) throws Exception {
//        // testStreamAPI();
//        // testStreamMap();
//        // testStreamPerformance();
//        testInt(2, 3, 4, 2, 3, 5, 1);
//        testOccurrence(2, 3, 4, 2, 3, 5, 1);
//        distinctSum(2, 3, 4, 2, 3, 5, 1);
//        testNestLambda();
//    }

    @Test
    public void main(){
        testInt(2, 3, 4, 2, 3, 5, 1);
        testOccurrence(2, 3, 4, 2, 3, 5, 1);
        distinctSum(2, 3, 4, 2, 3, 5, 1);

        try {
            testNestLambda();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChangeData() throws IOException {
        System.out.println(JacksonUtil.toJson(persons));
        persons.stream().forEach(p ->
            p.setAge(100)
        );
        System.out.println(JacksonUtil.toJson(persons));

        // 结论：当然可以修改原数据中的值
    }

    @Test
    public  void testStreamAPI() {
        // 打印年龄大于12的人
        System.out.println("使用顺序流串行打印");
        persons.stream().filter(p -> p.getAge() > 12)
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(p -> {
                    System.out.println(p);
                });
        System.out.println("使用并行流并行打印，即利用多核技术可将大数据通过多核并行处理");
        persons.parallelStream().filter(p -> p.getAge() > 12)
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(p -> {
                    System.out.println(p);
                });
    }

    @Test
    public  void testStreamMap() {
        // 应该用filter过滤，然后再使用map进行转换
        persons.parallelStream().map(p -> {
            if (p.getAge() > 18)
                return p;
            return null;
        }).collect(Collectors.toCollection(ArrayList::new)).forEach(p -> {
            if (p != null)
                System.out.println(p);
        });

        persons.parallelStream().filter(p -> p.getAge() > 18)
                .map(p -> new Adult(p))
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(p -> {
                    if (p != null)
                        System.out.println(p);
                });
    }

    public  void testStreamReduce() {
        persons.parallelStream().filter(p -> p.getAge() > 18)
                .map(p -> new Adult(p))
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(p -> {
                    if (p != null)
                        System.out.println(p);
                });
    }

    @Test
    public  void testStreamPerformance() {
        // 初始化一个范围100万整数流,求能被2整除的数字，toArray（）是终点方法
        long start1 = System.nanoTime();
        int a[] = IntStream.range(0, 1_000_000).filter(p -> p % 2 == 0)
                .toArray();
        System.out.printf("测试顺序流的性能: %.2fs",
                (System.nanoTime() - start1) * 1e-9);

        long start2 = System.nanoTime();
        int b[] = IntStream.range(0, 1_000_000).parallel()
                .filter(p -> p % 2 == 0).toArray();
        System.out.printf(" 测试并行流的性能: %.2fs",
                (System.nanoTime() - start2) * 1e-9);
        // 本机的测试结果是：测试顺序流的性能: 0.02s 测试并行流的性能: 0.01s
        // 在100万时，并发流快些，1000万，并发流反而会慢些，估计和线程的频繁切换有关（本机是8线程CPU）
    }

    public  void testInt(Integer... numbers) {
        List<Integer> l = Arrays.asList(numbers);
        List<Integer> r = l.stream()
                .map(e -> new Integer(e))
                .filter(e -> e > 2)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("testInt result is: " + r);
    }

    public  void testOccurrence(Integer... numbers) {
        List<Integer> l = Arrays.asList(numbers);
        Map<Integer, Integer> r = l
                .stream()
                .map(e -> new Integer(e))
                .collect(
                        Collectors.groupingBy(p -> p,
                                Collectors.summingInt(p -> 1)));
        System.out.println("testOccurrence result is: " + r);
    }

    public  void distinctSum(Integer... numbers) {
        List<Integer> l = Arrays.asList(numbers);
        int sum = l.stream()
                .map(e -> new Integer(e))
                .distinct()
                .reduce(0, (x, y) -> x + y); // equivalent to .sum()
        System.out.println("distinctSum result is: " + sum);
    }

    public  void testNestLambda() throws Exception{
        Callable<Runnable> c1 = () -> () -> { System.out.println("Nested lambda"); };
        c1.call().run();
        // 用在条件表达式中
        Callable<Integer> c2 = false ? (() -> 42) : (() -> 24);
        System.out.println(c2.call());
    }

}


class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "name:" + name + " age:" + age;
    }
}

class Adult extends Person {
    public Adult(Person p) {
        super(p.getName(), p.getAge());
    }
}
