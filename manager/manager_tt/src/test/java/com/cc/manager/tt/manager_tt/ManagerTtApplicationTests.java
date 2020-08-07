package com.cc.manager.tt.manager_tt;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ManagerTtApplicationTests {

    @Test
    void contextLoads() {
        int size = 10;
        // 为了防止读一个list读性能影响，各自读一个list
        List<Integer> list1 = new ArrayList<>(size);
        List<Integer> list2 = new ArrayList<>(size);
        List<Integer> list3 = new ArrayList<>(size);
        List<Integer> list4 = new ArrayList<>(size);
        List<Integer> list5 = new ArrayList<>(size);
        List<Integer> list6 = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list1.add(i);
            list2.add(i);
            list3.add(i);
            list4.add(i);
            list5.add(i);
            list6.add(i);
        }

        // 用线程池来执行
        ExecutorService executorService = Executors.newFixedThreadPool(6);

//        executorService.submit(()->{
//            long t = System.nanoTime();
//            List<String> result = new ArrayList<>(size);
//            for (int i = 0; i <list2.size(); i++) {
//                result.add(list2.get(i).toString());
//            }
//            System.out.println("for i i++ for time:      "+ (System.nanoTime()-t));
//        });
        for (Integer i : list3) {
            executorService.submit(() -> {
                long t = System.nanoTime();
                System.out.println("for i" + i.toString());
                System.out.println("for i:list time:         " + (System.nanoTime() - t));
            });
        }


//        executorService.submit(()->{
//            long t = System.nanoTime();
//            List<String> result = new ArrayList<>(size);
//            list4.forEach(i->{
//                result.add(i.toString());
//            });
//            System.out.println("forEach time:            " + (System.nanoTime()-t));
//        });
//        executorService.submit(()->{
//            long t = System.nanoTime();
//            List<String> result = new ArrayList<>(size);
//            list5.stream().forEach(i->{
//                result.add(i.toString());
//            });
//            System.out.println("stream forEach time:     " + (System.nanoTime()-t));
//        });
//        executorService.submit(()->{
//            long t = System.nanoTime();
//            List<String> result = new ArrayList<>(size);
//            Iterator<Integer> iterator = list6.iterator();
//            while (iterator.hasNext()){
//                result.add(iterator.next().toString());
//            }
//            System.out.println("Iterator time:           " + (System.nanoTime()-t));
//        });
//
//        executorService.submit(()->{
//            long t = System.nanoTime();
//            List<String> result = new ArrayList<>(size);
//            list1.stream().parallel().forEach(i->{
//                result.add(i.toString());
//            });
//            System.out.println("stream parallel time:    " + (System.nanoTime()-t));
//        });
        executorService.shutdown();
    }


    @Test
    void test() {
        JSONObject jsonOne = new JSONObject();
        JSONObject jsonTwo = new JSONObject();

        jsonOne.put("name", "kewen");
        jsonOne.put("age", "24");

        jsonTwo.put("name", "Dota");
        jsonTwo.put("hobbit2", "wow");

        JSONObject jsonThree = new JSONObject();

        jsonThree.putAll(jsonOne);
        jsonThree.putAll(jsonTwo);

        System.out.println(jsonThree.toString());
    }

}


