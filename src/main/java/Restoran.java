import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restoran {
    final int timeSleep = 2000;
    List<String> listCustomer = new ArrayList<>();
    List<String> listOrder = new ArrayList<>();
    String name;
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
    Condition condition1 = lock1.newCondition();
    Condition condition2 = lock2.newCondition();


    public void make() {
        lock1.lock();
        try {
            while (listCustomer.size() < 1) {
                condition1.await();
            }
            name = listCustomer.remove(0);
            System.out.printf("Официант %s принял заказ у %s\n", Thread.currentThread().getName(), name);
            waiterCook(name);
            System.out.printf("Официант %s несет заказ %s\n", Thread.currentThread().getName(), name);
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();

        }

    }

    public void take() {
        lock1.lock();
        try {
            condition1.signal();
            while (listOrder.size() < 1) {
                condition1.await();
            }
            listOrder.remove(0);
            System.out.printf("Посетитель %s ест\n", Thread.currentThread().getName());
            sleep();
            System.out.printf("Посетитель %s уходит\n", Thread.currentThread().getName());
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
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

    public void waiterCook(String name) {
        lock2.lock();
        try {
            condition2.signal();
            sleep();
            listOrder.add(name);
            condition2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock2.unlock();
        }

    }
}
