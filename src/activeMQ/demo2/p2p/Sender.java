package activeMQ.demo2.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
//��Ե�ģʽ
public class Sender {
	private static final int SEND_NUMBER = 5;  
    public static void main(String[] args) {  
       // ConnectionFactory �����ӹ�����JMS������������  
       ConnectionFactory connectionFactory;  
       // Connection ��JMS�ͻ��˵�JMS Provider������  
       Connection connection = null;  
        // Session��һ�����ͻ������Ϣ���߳�  
       Session session;  
       // Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.  
       Destination destination;  
       // MessageProducer����Ϣ������  
       MessageProducer producer;  
        // TextMessage message;  
        // ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��  
       connectionFactory = new ActiveMQConnectionFactory(  
              ActiveMQConnection.DEFAULT_USER,  
              ActiveMQConnection.DEFAULT_PASSWORD,  
              "tcp://localhost:61616");  
       try{  
           // ����ӹ����õ����Ӷ���  
           connection = connectionFactory.createConnection();  
           //����  
           connection.start();  
           //��ȡ��������  
           session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);  
           //��ȡsession��FirstQueue��һ����������queue   ��������
           destination = session.createQueue("FirstQueue");  
           // �õ���Ϣ�����ߡ������ߡ�  
           producer = session.createProducer(destination);  
           //���ò��־û�  
           producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  
           //������Ϣ    ѭ���﷢��5����Ϣ
           sendMessage(session, producer);  
           //session.commit();  
           connection.close();  
       }  
       catch(Exception e){  
           e.printStackTrace();  
       }finally{  
           if(null != connection){  
              try {  
                  connection.close();  
              } catch (JMSException e) {  
                  // TODO Auto-generatedcatch block  
                  e.printStackTrace();  
              }  
           }      
       }  
    }  
    public static void sendMessage(Session session, MessageProducer producer)throws Exception{  
       for(int i=1; i<=SEND_NUMBER; i++){  
           TextMessage message = session.createTextMessage("ActiveMQ������Ϣ"+i);  
           System.out.println("������Ϣ��ActiveMQ���͵���Ϣ"+i);  
           producer.send(message);  
       }  
    }  

}
