package proxy;


import java.util.Arrays;

public class Proxy {

    private long id;

    private String ip;

    private int port;

    private String[] schemes;

    private String[] methods;

    private float responseTime;

    private long lastValidateTime;

    private String location;

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Proxy(String ip, int port, String[] schemes, String location) {
        this.ip = ip;
        this.port = port;
        this.schemes = schemes;
        this.location = location;
    }

    public Proxy(String ip, int port, String[] schemes, String[] methods, String location) {
        this.ip = ip;
        this.port = port;
        this.schemes = schemes;
        this.methods = methods;
        this.location = location;
    }

    public Proxy(String ip, int port, String[] schemes, String[] methods, float responseTime) {
        this.ip = ip;
        this.port = port;
        this.schemes = schemes;
        this.methods = methods;
        this.responseTime = responseTime;
    }

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }

    public long getLastValidateTime() {
        return lastValidateTime;
    }

    public void setLastValidateTime(long lastValidateTime) {
        this.lastValidateTime = lastValidateTime;
    }

    public String[] getSchemes() {
        return schemes;
    }

    public void setSchemes(String[] schemes) {
        this.schemes = schemes;
    }

    public String[] getMethods() {
        return methods;
    }

    public void setMethods(String[] methods) {
        this.methods = methods;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", schemes=" + Arrays.toString(schemes) +
                ", methods=" + Arrays.toString(methods) +
                ", responseTime=" + responseTime +
                ", lastValidateTime=" + lastValidateTime +
                ", location='" + location + '\'' +
                '}';
    }
}
