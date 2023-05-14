

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class access {
        public static void main(String[] args) {
            SynContainer container = new SynContainer();

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(new Producer(container));
            executor.execute(new Consumer(container));
            executor.shutdown();
        }

    }

    // 生产者
    class Producer extends Thread{
        SynContainer container;
        public Producer(SynContainer container){
            this.container = container;
        }

        //
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                container.push(new expressdelivery(i));
                System.out.println("存放了"+i+"个商品");

            }
        }
    }

    // 消费者
    class Consumer extends Thread{
        SynContainer container;
        public Consumer(SynContainer container){
            this.container = container;
        }

        //消费
        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                System.out.println("取出了"+container.pop().id+"个快递");
            }
        }
    }

    // 产品
    class expressdelivery{
        int id; //产品编号

        public expressdelivery(int id) {
            this.id = id;
        }
    }

    // 缓冲区
    class SynContainer {
        // 需要一个容器大小
        expressdelivery[] expressdeliverys = new expressdelivery[3];
        //容器计数器
        int count = 0;

        //===========生产者放入产品==================
        public synchronized void push(expressdelivery expressdelivery) {
            //如果容器满了,就需要等待消费消费
            if (count == expressdeliverys.length) {
                //通知消费者消费,生产等待
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //如果没有满,我们就需要丢入产品
            expressdeliverys[count] = expressdelivery;
            count++;
            //可以通知消费者消费了
            this.notifyAll();
        }

        //===========消费者消费产品=================
        public synchronized expressdelivery pop() {
            // 判防能否消费
            if (count == 0) {
                //等待生产者生产,消费者等待
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //如果可以消费
            count--;
            expressdelivery chicken = expressdeliverys[count];

            // 完了,通知生产者生产
            this.notifyAll();
            return chicken;
        }
    }

