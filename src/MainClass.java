import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainClass {
    public static final int CARS_COUNT = 5;
    public static CyclicBarrier CB_READY;
    public static CountDownLatch CDL_PREP;
    public static CountDownLatch CDL_FINISH;
    public static Semaphore SMP_AMOUNT;
    public static Lock LOCK_WIN;


    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

        CB_READY = new CyclicBarrier(CARS_COUNT); // барьеер на количество авто, готовых к старту
        CDL_PREP = new CountDownLatch(CARS_COUNT); // счетчик на количество готовых авто к старту
        CDL_FINISH = new CountDownLatch(CARS_COUNT); // счетчик на количество авто, завершивших гонку
        SMP_AMOUNT = new Semaphore(CARS_COUNT/2); // семафор на ограничние по числу авто в тоннелях
        LOCK_WIN = new ReentrantLock(); // замок на вывод победителя

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            CDL_PREP.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");


        try {
            CDL_FINISH.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}