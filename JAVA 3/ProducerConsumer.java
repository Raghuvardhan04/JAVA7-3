import java.util.LinkedList;

public class ProducerConsumer {
    private final LinkedList<Integer> buffer;
    private final int capacity;

    public ProducerConsumer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
    }

    public void produce() throws InterruptedException {
        int value = 0;

        while (true) {
            synchronized (this) {
                while (buffer.size() == capacity) {
                    wait();
                }

                buffer.add(value);
                System.out.println("Producer produced-" + value);
                value++;
                notify();
            }

            Thread.sleep(1000);
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (buffer.isEmpty()) {
                    wait();
                }

                int value = buffer.removeFirst();
                System.out.println("Consumer consumed-" + value);
                notify();
            }

            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ProducerConsumer pc = new ProducerConsumer(2);

        Thread producerThread = new Thread(() -> {
            try {
                pc.produce();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                pc.consume();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }
        });

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }
}
