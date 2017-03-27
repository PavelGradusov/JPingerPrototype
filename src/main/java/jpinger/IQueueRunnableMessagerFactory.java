package jpinger;

import java.util.Queue;

/**
 * Created by pavel on 26.03.2017.
 */
public interface IQueueRunnableMessagerFactory {

    IQueueRunnableMessager getInstance(Queue<IMessage> requestQueue, Queue<IMessage> responseQueue);

}
