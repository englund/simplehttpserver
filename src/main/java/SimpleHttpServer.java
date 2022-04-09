import java.io.*;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        String basePath = args[1];
        Server server = new Server(port, basePath);
        server.start();
    }
}
