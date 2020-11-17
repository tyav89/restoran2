public class Main {
    public static void main(String[] args) {
        Restoran restoran = new Restoran();

        new Cook(restoran);
        new Waiter(restoran, "1");
        new Waiter(restoran, "2");
        new Waiter(restoran, "3");

        new Customer(restoran, "1");
        new Customer(restoran, "2");
        new Customer(restoran, "3");
        new Customer(restoran, "4");
    }
}
