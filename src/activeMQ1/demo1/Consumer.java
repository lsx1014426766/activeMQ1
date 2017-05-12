package activeMQ1.demo1;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	private static String brokerURL = "tcp://localhost:61616";  
    private static transient ConnectionFactory factory;  
    private transient Connection connection;  
    private transient Session session;  
      
    public Consumer() throws JMSException {  //初始化
        factory = new ActiveMQConnectionFactory(brokerURL);  
        connection = factory.createConnection();  
        connection.start();  
      //1是否启用事务，2确认接收模式
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
    }  
      
    public void close() throws JMSException {  
    	//
        if (connection != null) {  
            connection.close();  
        }  
    }      
      
    public static void main(String[] args) throws JMSException {  
        Consumer consumer = new Consumer();  
        for (String stock : args) {  
        	//topic://STOCKS.ORCL
            Destination destination = consumer.getSession().createTopic("STOCKS." + stock);  
            MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);  
            //交给监听器去处理消息
            messageConsumer.setMessageListener(new Listener());  
        }  
    }  
      
    public Session getSession() {  
        return session;  
    }  

}
