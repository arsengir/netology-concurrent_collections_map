import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {
    private static final int SIZE_MAP = 700;
    private static final int COUNT_THREAD = 4;

    public static void main(String[] args) throws InterruptedException {

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());

        addMap(concurrentHashMap, "ConcurrentHashMap");
        addMap(hashMap, "HashMap");

        getMap(hashMap, "HashMap");
        getMap(concurrentHashMap, "ConcurrentHashMap");
    }

    private static void addMap(Map<Integer, Integer> map,  String typeMap) {
        ExecutorService threadPollPut = Executors.newFixedThreadPool(COUNT_THREAD);
        long startTime = System.currentTimeMillis();
        IntStream.range(0, SIZE_MAP).forEach(i -> threadPollPut.submit(() -> map.put(i, i)));
        while (map.size() != SIZE_MAP){
        }
        long finishTime = System.currentTimeMillis();
        System.out.printf("Запись %s %d ms. \n", typeMap, (finishTime - startTime));
        threadPollPut.shutdown();
    }

    private static void getMap(Map<Integer, Integer> map, String typeMap) throws InterruptedException {

        Runnable get = () -> {
            for (int i = 0; i < SIZE_MAP; i++){
                map.get(i);
            }
        };

        Thread threadGet1 = new Thread(get);
        Thread threadGet2 = new Thread(get);
        Thread threadGet3 = new Thread(get);
        Thread threadGet4 = new Thread(get);

        long startTime = System.currentTimeMillis();
        threadGet1.start();
        threadGet2.start();
        threadGet3.start();
        threadGet4.start();
        threadGet1.join();
        threadGet2.join();
        threadGet3.join();
        threadGet4.join();
        long finishTime = System.currentTimeMillis();
        System.out.printf("Чтение %s %d ms. \n", typeMap, (finishTime - startTime));
    }

}
