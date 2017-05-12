package activeMQ.demo3.jndi;

import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
/**
 * 这部分是用Activemq本身自带jndi构建topic  
 * @author Administrator
 *
 */
public class Publisher {

    private  Session session;
    private  MessageProducer publisher ;
    private Connection connection;
    
    //初始化工作
    public void initialize() throws URISyntaxException, Exception
    {
    /*这部分注释是原始方式构建topic*/
        /*ConnectionFactory connectFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        connection = connectFactory.createConnection();
        session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Destination destination =  session.createTopic("topic1");
        publisher = session.createProducer(destination);
        connection.start();*/
    /*这部分是用Activemq本身自带jndi构建topic*/    
        // create a new intial context, which loads from jndi.properties file
        InitialContext ctx = new InitialContext();
        // lookup the connection factory
        //InitialContext的构造器会在类路径中找jndi.properties文件，如果找到，通过里面的属性，创建初始上下文
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        // create a new TopicConnection for pub/sub messaging
         connection = factory.createConnection();
        // lookup an existing topic
        javax.jms.Topic mytopic = (javax.jms.Topic)ctx.lookup("MyTopic");
        // create a new TopicSession for the client
        session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
        // create a new subscriber to receive messages
         publisher = session.createProducer(mytopic);

        connection.start();
    }
    public void sendText(String Message)
    {
        try {
            TextMessage text = session.createTextMessage(Message);
            System.out.println("Sending message:"+text.getText());     
            publisher.send(text);

        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void submit() throws JMSException
    {
        session.commit();
    }
    // 关闭连接     
    public void close() throws JMSException {     
        System.out.println("Producer:->Closing connection");     
        if (publisher != null)     
            publisher.close();     
        if (session != null)     
            session.close();     
        if (connection != null)     
            connection.close();     
    }
}
