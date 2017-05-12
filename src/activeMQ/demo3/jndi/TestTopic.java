package activeMQ.demo3.jndi;

import java.net.URISyntaxException;


public class TestTopic {
	   public static void main(String[] args) throws URISyntaxException, Exception
	    {
	        Publisher producer = new Publisher();
	        Subscriber consumer = new Subscriber("1");
	        Subscriber consumer1 = new Subscriber("2");
	        producer.initialize();
	        System.out.println("consumer1开始监听");
	        consumer.recive();
	        System.out.println("consumer2开始监听");
	        consumer1.recive();

	        Thread.sleep(500);   
	        for(int i=0;i<3;i++)
	        {
	            producer.sendText("Hello, world!"+i);     
	        }
	        producer.submit();
	        producer.close();

	    }

	}
/**
 *  javax.naming.NoInitialContextException: Need to specify class name in environment or system property, or as an applet parameter, or in an application resource file:  java.naming.factory.initial
 * */
 