package jpinger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by pavel on 26.03.2017.
 */
public class HostsStorage implements IStorage{

    Logger logger = LoggerFactory.getLogger(HostsStorage.class);

    private Map<String, Host> hosts = new HashMap<String, Host>();
    private Map<String, Date> lastGetDate = new HashMap<String, Date>();
    private static final int waitTimeout = 2000;

    private ISaver outSaver;
    private List<String> inetHosts;
//    Arrays.asList("mail.ru",
//            "yandex.ru",
//            "billing.ru",
//            "stackoverflow.com",
//            "ya.ru",
//            "voffka.com",
//            "yaplakal.com",
//            "javacodex.com",
//            "docs.oracle.com",
//            "oracle.com",
//            "java.com",
//            "rutracker.org",
//            "127.0.0.1",
//            "192.168.1.1");

    public HostsStorage() {
    }

    private void init() {
        // заполняем список хостов
        for (int i = 0; i < inetHosts.size(); i++) {
            try {
                Host newHost = new Host(inetHosts.get(i), waitTimeout);
                hosts.put(inetHosts.get(i), newHost);
            } catch (UnknownHostException e) {
                logger.debug("Host " + inetHosts.get(i) + " is not valid and will removed from list by reason:" + e);
            }
        }

    }

    public IMessage getNext(Date outOfTimeDate) {
        if(hosts.size() == 0){
            init();
        }
        IMessage retVal = null;
        // check hosts list
        for (Host host : hosts.values()) {
            String hostName = host.getIpAddress();
            if (!lastGetDate.containsKey(hostName) || lastGetDate.get(hostName).before(outOfTimeDate)) {
                retVal = host.getCopy();
                lastGetDate.put(hostName, new Date());
                break;
            }
        }
        return retVal;
    }

    public void put(IMessage message) {
        saveOutOfStorage(message);
        Host host = (Host) message;
        String hostName = host.getIpAddress();
        hosts.put(hostName, host);
        logger.debug(host.toString());
    }

    private void saveOutOfStorage(IMessage message) {
        if (outSaver != null) {
            outSaver.save(message);
        }
    }

    public void printCurrentResult() {
        for (Host host : hosts.values()) {
            logger.debug(host.toString());
        }
        logger.debug("=========================================================================================");
    }

    public void setOutSaver(ISaver outSaver) {
        this.outSaver = outSaver;
    }

    public void setInetHosts(String inetHosts) {
        List<String> list = new ArrayList<String>();
        for (String s : Arrays.asList(inetHosts.split(","))) {
            list.add(s.trim());
        }
        this.inetHosts = list;
    }

}
