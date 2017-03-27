package jpinger;

import java.util.Queue;

/**
 * Created by pavel on 26.03.2017.
 */
public interface IQueueRunnableMessager extends Runnable {

    void setQueues(Queue<IMessage> requestQueue, Queue<IMessage> responseQueue);

}
