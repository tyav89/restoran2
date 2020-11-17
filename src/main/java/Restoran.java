import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 public class Restoran {
    final int timeSleep = 1000;
    List<String> listCustomer = new ArrayList<>();
    List<String> listOrder = new ArrayList<>();
    List<String> listMakeOrder = new ArrayList<>();
    String name;
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
    private final Lock lock3 = new ReentrantLock();
    private final Lock lock4 = new ReentrantLock();

    Condition condition1 = lock1.newCondition();
    Condition condition2 = lock2.newCondition();
    Condition condition3 = lock3.newCondition();
    Condition condition4 = lock4.newCondition();

    public void make() {
        lock1.lock();
        try {
            while (listCustomer.size() < 1){
                condition1.await();
            }
            name = listCustomer.remove(0);

            System.out.printf("Официант %s принял заказ у %s\n", Thread.currentThread().getName(), name);
            listOrder.add(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
        waiterCook();
        carries();

    }

    public void take() {
        readyToOrder();
        lock3.lock();
        try {
            while (listMakeOrder.size() < 1) {
                condition3.await();
            }
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
            while (listOrder.size() < 1){
                condition2.await();
            }
            System.out.println("Повар готовит блюдо");
            sleep();
            System.out.println("Повар закончил готовить");
            listMakeOrder.add(listOrder.remove(0));
            order();
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

    private void order() {
        lock4.lock();
        try {
            condition4.signal();
        }finally {
            lock4.unlock();
        }
    }

    private void carries() {
        lock4.lock();
        try {
            condition4.await();
            System.out.printf("Официант %s несет заказ %s\n", Thread.currentThread().getName(), name);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock4.unlock();
        }
        orderReady();
    }


}
