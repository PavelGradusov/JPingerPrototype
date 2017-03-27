import jpinger.IDispatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by pavel on 26.03.2017.
 */
public class JPinger {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("java-pinger.xml");
        IDispatcher dispatcher = (IDispatcher) context.getBean("dispatcher");
        dispatcher.run();
    }

}
