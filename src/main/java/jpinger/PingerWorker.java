package jpinger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

/**
 * Created by pavel on 26.03.2017.
 */
public class PingerWorker implements IQueueRunnableMessager {

    Logger logger = LoggerFactory.getLogger(PingerWorker.class);

    private static int counter = 0;
    private int instance = counter++;

    private Queue<IMessage> requestQueue;
    private Queue<IMessage> responseQueue;

    public PingerWorker() {
        logger.debug("PingerWorker instance " + instance + " created");
    }

    public void run() {
        logger.debug("Run PingerWorker instance " + instance);

        IMessage hostToCheck;
        try {
            while(true){
                if ((hostToCheck = requestQueue.poll()) == null) {
                    Thread.sleep(100);
                } else {
                    check(hostToCheck);
                }
            }
        } catch (InterruptedException e) {
            logger.debug("Shutdown PingerWorker instance " + instance);
        }

    }

    private void check(IMessage message) {
        Host host = (Host) message;
        logger.debug("Instance " + instance + " check host " + host.getIpAddress());
        host.makeAction();
        responseQueue.offer(host);
    }

    public void setQueues(Queue<IMessage> requestQueue, Queue<IMessage> responseQueue) {
        this.requestQueue = requestQueue;
        this.responseQueue = responseQueue;
    }
}
