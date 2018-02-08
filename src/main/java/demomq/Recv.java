package demomq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Recv implements Runnable {
    private  final  static  String QUEUE_NAME="test_exchange_xin"+Math.random();
    private final static String EXCHANGE_NAME = "test_exchange_fanout";
    Connection connection = null;
    public  Recv(  Connection connection){
        this.connection=connection;
    }
    public void run() {
        try {

            // 获取到连接以及mq通道

            final Channel channel = connection.createChannel();
            // 声明队列
            channel.queueDeclare(QUEUE_NAME,false, false, false, null);
            // 绑定队列到交换机
            // channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "item.update");
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            // 同一时刻服务器只会发一条消息给消费者
            channel.basicQos(1);
            // 定义队列的消费者
            final Consumer consumer = new DefaultConsumer(channel) {
                // 消息到达 触发这个方法
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body, "utf-8");
                    System.out.println(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // 手动回执
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}