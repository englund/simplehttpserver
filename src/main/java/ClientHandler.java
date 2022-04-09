import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final String basePath;
    private final Socket socket;

    public ClientHandler(Socket socket, String basePath) {
        this.socket = socket;
        this.basePath = basePath;
    }

    public void handleRequest() {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();

            File file = new File(basePath, getPath(inputStream));
            if (file.exists()) {
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                outputStream.write(httpResponse.getBytes());
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.transferTo(outputStream);
            } else {
                outputStream.write(("HTTP/1.1 404 Not Found \r\n\r\n").getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPath(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String firstLine = br.readLine();
        String path = firstLine.split(" ")[1];
        return path;
    }

    @Override
    public void run() {
        this.handleRequest();
    }
}
