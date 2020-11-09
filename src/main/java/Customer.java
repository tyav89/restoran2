public class Customer implements Runnable{
    Restoran restoran;

    public Customer(Restoran restoran, String name) {
        this.restoran = restoran;
        System.out.printf("Посетитель %s в ресторане!\n", name);
        restoran.listCustomer.add(name);
        new Thread(this, name).start();
    }

    @Override
    public void run() {
        restoran.take();

    }
}
