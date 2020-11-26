public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
//        CyclicBarrier cb = new CyclicBarrier(4);
//        CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
//            cdl.countDown();
            System.out.println(this.name + " готов");
            MainClass.CDL_PREP.countDown();
            MainClass.CB_READY.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

//        if (MainClass.CDL_FINISH.getCount() == MainClass.CARS_COUNT) {
//            System.out.println(this.name + " WIN!!!");
//        }

        // блокировка критической секции вывода побидителя
        try {
            MainClass.LOCK_WIN.lock();
            if (MainClass.CDL_FINISH.getCount() == MainClass.CARS_COUNT) {
                System.out.println(this.name + " WIN!!!");
            }
        } finally {
            MainClass.LOCK_WIN.unlock();
        }

        MainClass.CDL_FINISH.countDown();

    }
}