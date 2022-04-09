import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
            Request request = getRequest(inputStream);

            File file = new File(basePath, request.getPath());
            if (file.exists()) {
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
                outputStream.write(httpResponse.getBytes());
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.transferTo(socket.getOutputStream());
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

    private Request getRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
        }

        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>();
        for (int h = 2; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }
        return new Request(method, path, version, host, headers);
    }

    @Override
    public void run() {
        this.handleRequest();
    }
}
