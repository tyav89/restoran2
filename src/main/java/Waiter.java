public class Waiter implements Runnable {
    Restoran restoran;

    public Waiter(Restoran restoran, String name) {
        this.restoran = restoran;
        System.out.printf("Официант %s на работ!\n", name);
        new Thread(this, name).start();
    }

    @Override
    public void run() {
        while (true)restoran.make();
    }
}
