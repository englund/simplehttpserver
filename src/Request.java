import java.util.List;

public class Request {
    private String method;
    private final String path;
    private final String version;
    private final String host;
    private final List<String> headers;

    public Request(String method, String path, String version, String host, List<String> headers) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.host = host;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
