package activeMQ.demo3.jndi;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Subscriber implements MessageListener {

    private String name = "";


   // private String subject = "TOOL.DEFAULT";     

   // private Destination destination = null;     

    private Connection connection = null;     

    private Session session = null;     

    private MessageConsumer consumer = null; 
    
    Subscriber(String name){
        this.name=name;
    }

    public  void initialize() throws JMSException, NamingException
    {
        /*采用原始方式构建topic*/
        /*ConnectionFactory connectFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination =  session.createTopic("topic1");
        consumer = session.createConsumer(destination);
        connection.start();*/

        /*利用activemq本身的jndi构建*/
        // create a new intial context, which loads from jndi.properties file
        InitialContext ctx = new InitialContext();
        // lookup the connection factory
        ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
        // create a new TopicConnection for pub/sub messaging
         connection = factory.createConnection();
        // lookup an existing topic
        javax.jms.Topic mytopic = (javax.jms.Topic)ctx.lookup("MyTopic");
        // create a new TopicSession for the client
        session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
        // create a new subscriber to receive messages
        consumer = session.createConsumer(mytopic);

        connection.start();

    }

    public void recive() throws NamingException
    {
        try {
            initialize();
            System.out.println("Consumer("+name+"):->Begin listening...");     
         // 开始监听     
           consumer.setMessageListener(this);     
           /* Message message = consumer.receive(); //主动接收消息
            System.out.println("consumer recive:"+((TextMessage)message).getText());
*/
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }     
    }
    @Override
    public void onMessage(Message arg0) {
        // TODO Auto-generated method stub
        try{
            if(arg0 instanceof TextMessage)
            {
                TextMessage txtMsg = (TextMessage) arg0; 
                System.out.println("consumer("+name+") recive:"+txtMsg.getText());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void submit() throws JMSException
    {
        session.commit();
    }
     // 关闭连接     
    public void close() throws JMSException {     
        System.out.println("Consumer:->Closing connection");     
        if (consumer != null)     
            consumer.close();     
        if (session != null)     
            session.close();     
        if (connection != null)     
            connection.close();     
    } 

}