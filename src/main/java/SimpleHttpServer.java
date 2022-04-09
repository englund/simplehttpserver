import java.io.*;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        int port = 8880;
        Server server = new Server(port);
        server.start();
    }
}
