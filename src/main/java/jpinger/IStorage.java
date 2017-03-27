package jpinger;

import java.util.Date;

/**
 * Created by pavel on 26.03.2017.
 */
public interface IStorage {

    IMessage getNext(Date outOfTimeDate);

    void put(IMessage message);

}
