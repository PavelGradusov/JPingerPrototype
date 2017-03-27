package jpinger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by pavel on 26.03.2017.
 */
public class Host implements IMessage{
    private final InetAddress address;
    private final int waitTimeout;
    private Long reachableTime;
    private Boolean isReachable;
    private Date lastCheckDate;
    private String errorMessage;
    private String ipAddress;

    public Host(String address, int waitTimeout) throws UnknownHostException {
        this.waitTimeout = waitTimeout;
        this.ipAddress = address;
        this.address = InetAddress.getByName(address);
    }

    public Host(InetAddress address, String ipAddress, int waitTimeout) {
        this.address = address;
        this.waitTimeout = waitTimeout;
        this.ipAddress = ipAddress;
    }

    public IMessage getCopy(){
        return new Host(address, ipAddress, waitTimeout);
    }

    public void makeAction() {
        lastCheckDate = new Date();
        try {
            long startTime = System.currentTimeMillis();
            isReachable = address.isReachable(waitTimeout);
            reachableTime = System.currentTimeMillis() - startTime;
            errorMessage = null;
        } catch (IOException e) {
            isReachable = false;
            errorMessage = stackTraceToString(e);
            reachableTime = null;
        }
    }

    public String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public InetAddress getAddress() {
        return address;
    }

    public Long getReachableTime() {
        return reachableTime;
    }

    public Boolean getReachable() {
        return isReachable;
    }

    public Date getLastCheckDate() {
        return lastCheckDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "Host{" +
                "address=" + address +
                ", waitTimeout=" + waitTimeout +
                ", reachableTime=" + reachableTime +
                ", isReachable=" + isReachable +
                ", lastCheckDate=" + lastCheckDate +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
