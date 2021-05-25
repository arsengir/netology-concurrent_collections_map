import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int SIZE_MAP = 7000000;
        final int COUNT_THREAD = 4;

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap();
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());


        ExecutorService threadPollPut = Executors.newFixedThreadPool(COUNT_THREAD);

        long startTime = System.currentTimeMillis();
        IntStream.range(0, SIZE_MAP).forEach(i -> threadPollPut.submit(() -> concurrentHashMap.put(i, i)));
        while (concurrentHashMap.size() != SIZE_MAP){
        }
        long finishTime = System.currentTimeMillis();
        System.out.println("Запись ConcurrentHashMap " + (finishTime - startTime) + " ms.");

        startTime = System.currentTimeMillis();
        IntStream.range(0, SIZE_MAP).forEach(i -> threadPollPut.submit(() -> hashMap.put(i,i)));
        while (hashMap.size() != SIZE_MAP){
        }
        finishTime = System.currentTimeMillis();
        System.out.println("Запись HashMap " + (finishTime-startTime) + " ms.");
        threadPollPut.shutdown();

        Runnable getHashMap = () -> {
            for (int i = 0; i < SIZE_MAP; i++){
                hashMap.get(i);
            }
        };
        Runnable getConcurrentHashMap = () -> {
            for (int i = 0; i < SIZE_MAP; i++){
                concurrentHashMap.get(i);
            }
        };

        getMap(getHashMap, "HashMap");
        getMap(getConcurrentHashMap, "ConcurrentHashMap");
    }

    private static void getMap(Runnable run, String typeMap) throws InterruptedException {
        long startTime;
        long finishTime;
        Thread threadGet1 = new Thread(run);
        Thread threadGet2 = new Thread(run);
        Thread threadGet3 = new Thread(run);
        Thread threadGet4 = new Thread(run);

        startTime = System.currentTimeMillis();
        threadGet1.start();
        threadGet2.start();
        threadGet3.start();
        threadGet4.start();
        threadGet1.join();
        threadGet2.join();
        threadGet3.join();
        threadGet4.join();
        finishTime = System.currentTimeMillis();
        System.out.printf("Чтение %s %d ms. \n", typeMap, (finishTime - startTime));
    }

}
