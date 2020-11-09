public class Cook implements Runnable{
    Restoran restoran;

    public Cook(Restoran restoran) {
        this.restoran = restoran;
        System.out.println("Повар на работе");
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true)restoran.cook();
    }
}
