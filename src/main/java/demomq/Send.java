package demomq;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class Send {
    private final static String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        // 声明exchange
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //channel.basicQos(1);
        Recv r1 = new Recv(connection);
        Thread t = new Thread(r1);
        t.setDaemon(true);
        // 消息内容
        //  String message = "id=1001";
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎加入rabbitMQ聊天室,输入q退出，请输入昵称：");
        String name = scanner.nextLine();
        if (!"q".equals(name)) {
            t.start();
            System.out.println("hi  " + name + ",现在可以和大家聊天了");
            boolean flag = true;
            while (flag) {
                String message = scanner.nextLine();
                if ("q".equals(message)) {
                    message = name+":溜了溜了";
                    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                    Thread.sleep(1200);
                    channel.close();
                    connection.close();
                    scanner.close();
                    flag = false;
                }
                if (flag) {
                    String send = name + " said:" + message;
                    channel.basicPublish(EXCHANGE_NAME, "", null, send.getBytes());
                }
            }
        }else {
            channel.close();
            connection.close();
            scanner.close();
        }
    }
}
