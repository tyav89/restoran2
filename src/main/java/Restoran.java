import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restoran {
    final int timeSleep = 2000;
    List<String> listCustomer = new ArrayList<>();
    String name;
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
    private final Lock lock3 = new ReentrantLock();

    Condition condition1 = lock1.newCondition();
    Condition condition2 = lock2.newCondition();
    Condition condition3 = lock3.newCondition();


    public void make() {
        lock1.lock();
        try {
            condition1.await();
            name = listCustomer.remove(0);
            System.out.printf("Официант %s принял заказ у %s\n", Thread.currentThread().getName(), name);
            waiterCook();
            carries();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();

        }
    }

    public void take() {
        readyToOrder();
        lock3.lock();
        try {
            condition3.await();
            System.out.printf("Посетитель %s ест\n", Thread.currentThread().getName());
            sleep();
            System.out.printf("Посетитель %s уходит\n", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock3.unlock();
        }
    }

    public void cook() {
        lock2.lock();
        try {
            condition2.await();
            System.out.println("Повар готовит блюдо");
            sleep();
            System.out.println("Повар закончил готовить");
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock2.unlock();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(timeSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waiterCook() {
        lock2.lock();
        try {
            condition2.signal();
        } finally {
            lock2.unlock();
        }
    }

    private void orderReady() {
        lock3.lock();
        try {
            condition3.signal();
        } finally {
            lock3.unlock();
        }
    }

    private void readyToOrder() {
        lock1.lock();
        try {
            condition1.signal();
        } finally {
            lock1.unlock();
        }
    }

    private void carries() {
        lock2.lock();
        try {
            condition2.await();
            System.out.printf("Официант %s несет заказ %s\n", Thread.currentThread().getName(), name);
            orderReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock2.unlock();
        }
    }


}
