package demomq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {
    public static Connection getConnection() throws IOException, TimeoutException {
//定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//设置服务地址
        factory.setHost("localhost");
//端口
        factory.setPort(5672);//amqp协议 端口 类似与mysql的3306
//设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/vhost_lan");
        factory.setUsername("lanjian");
        factory.setPassword("lanjian");
// 通过工程获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
