package jpinger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pavel on 26.03.2017.
 */
public class Dispatcher implements IDispatcher{

    Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private Queue<IMessage> requestQueue = new ConcurrentLinkedQueue<IMessage>();
    private Queue<IMessage> responseQueue = new ConcurrentLinkedQueue<IMessage>();

    IQueueRunnableMessagerFactory messagerFactory;
    ExecutorService executorService;

    IStorage storage;

    int pingTimeoutSeconds = 15;
    int numberOfThreads = 1;
    int pauseTimeMilliseconds = 0;


    public void run() {

        makeWorkers(numberOfThreads);

        try {
            runMainCycle();
        } catch (InterruptedException e) {
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                //just wait
            }
            logger.debug("Shutdown Dispatcher");
        }
    }

    private void runMainCycle() throws InterruptedException {
        while (true) {
            int sended = sendHostsForPing();
            int answered = checkRequests();

            if (sended == 0 && answered == 0) {
                logger.debug(new Date() + " Nothing to Do, Will Wait");
                Thread.sleep(pauseTimeMilliseconds);
            }
        }
    }

    private int checkRequests() {
        IMessage next;
        int counter = 0;
        while ((next = responseQueue.poll()) != null) {
            storage.put(next);
            counter++;
        }
        return counter;
    }

    private int sendHostsForPing() {
        IMessage nextHost;
        int counter = 0;
        while ((nextHost = storage.getNext(new Date(new Date().getTime() - pingTimeoutSeconds * 1000))) != null) {
            requestQueue.offer(nextHost);
            counter++;
        }
        return counter;
    }

    private void makeWorkers(int numberOfThreads) {
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            //create workers
            executorService.submit(messagerFactory.getInstance(requestQueue, responseQueue));
        }
    }

    public void setPingTimeoutSeconds(int pingTimeoutSeconds) {
        this.pingTimeoutSeconds = pingTimeoutSeconds;
    }

    public void setStorage(HostsStorage storage) {
        this.storage = storage;
    }

    public void setMessagerFactory(IQueueRunnableMessagerFactory messagerFactory) {
        this.messagerFactory = messagerFactory;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public void setPauseTimeMilliseconds(int pauseTimeMilliseconds) {
        this.pauseTimeMilliseconds = pauseTimeMilliseconds;
    }
}
