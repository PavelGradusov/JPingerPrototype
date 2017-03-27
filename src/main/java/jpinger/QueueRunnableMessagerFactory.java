package jpinger;

import java.util.Queue;

/**
 * Created by pavel on 26.03.2017.
 */
public class QueueRunnableMessagerFactory implements IQueueRunnableMessagerFactory{


    public IQueueRunnableMessager getInstance(Queue<IMessage> requestQueue, Queue<IMessage> responseQueue) {
        IQueueRunnableMessager retVal = new PingerWorker();
        retVal.setQueues(requestQueue, responseQueue);
        return retVal;
    }
}
