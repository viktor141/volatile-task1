package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger nik3 = new AtomicInteger();
    public static AtomicInteger nik4 = new AtomicInteger();
    public static AtomicInteger nik5 = new AtomicInteger();


    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        ThreadGroup threadGroup = new ThreadGroup("counters");

        new Thread(threadGroup, () -> {
            for (String s : texts) {
                if (s.equals(new StringBuilder(s).reverse().toString())) {
                    increment(s.length());
                }
            }

        }).start();

        new Thread(threadGroup, () -> {
            for (String s : texts) {
                char letter = s.charAt(0);
                boolean flag = true;
                for (int i = 1; i < s.length(); i++) {
                    if (letter != s.charAt(i)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) increment(s.length());
            }

        }).start();

        new Thread(threadGroup, () -> {
            for (String s : texts) {
                byte[] bytes = s.getBytes();
                boolean flag = true;
                for (int i = 0; i < bytes.length - 1; i++) {
                    if (bytes[i] > bytes[i + 1]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) increment(s.length());

            }

        }).start();

        while (threadGroup.activeCount() != 0) ;


        System.out.printf("Красивых слов с длиной 3: %d шт\n", nik3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт\n", nik4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт\n", nik5.get());
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void increment(int i) {
        switch (i) {
            case 3 -> nik3.incrementAndGet();
            case 4 -> nik4.incrementAndGet();
            case 5 -> nik5.incrementAndGet();
        }
    }
}
