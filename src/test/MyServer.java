package test;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {
    private final int port;
    private final ClientHandler  clientHandler;
    private volatile boolean run;

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.run = true;
    }

    private void runServer() throws IOException {
        ServerSocket myServer = new ServerSocket(port);
        myServer.setSoTimeout(1000);
        Socket aClient = null;
        try {
            while (run) {
                try {
                    aClient = myServer.accept();
                    try {
                        clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                        clientHandler.close();
                    } catch (IOException ignored) {
                    }
                } catch (SocketTimeoutException ignored) {
                }
            }
        }
        finally {
            clientHandler.close();
            assert aClient != null;
            if (!aClient.isClosed()){aClient.close();}
            if (!myServer.isClosed()){myServer.close();}
        }
    }

    public void start(){
        new Thread(() -> {
            try {
                runServer();
            } catch (IOException ignored) {}
        }).start();
    }
    public void close(){
        run = false;
    }
}
