import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final Thread serverThread = new Thread(this);
    private final ExecutorService executor;
    private final int port;
    private final ServerSocket server;
    private boolean serverRunning;

    public Server(int port) throws IOException {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(10);
        this.server = new ServerSocket(this.port);
    }

    public void start() {
        this.serverRunning = true;
        this.serverThread.start();
    }

    @Override
    public void run() {
        System.out.println(String.format("Listening for connection on port %d", this.port));

        while (serverRunning){
            try {
                Socket socket = server.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                executor.submit(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Server stopped");
    }
}
