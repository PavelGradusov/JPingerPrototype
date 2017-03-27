package jpinger;

/**
 * Created by pavel on 26.03.2017.
 */
public class HostConsoleSaver implements ISaver {

    public void save(IMessage message) {
        Host host = (Host) message;

        StringBuilder sb = new StringBuilder();
        sb.append("richable:" + host.getReachable() +
                " time:" + host.getReachableTime() +
                " hostName:" + host.getIpAddress() +
                " errorMessage:" + host.getErrorMessage());
        System.out.println(sb.toString());
    }

}
