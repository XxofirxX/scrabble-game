package model.host;

import server.ClientHandler;
import server.ServerPolicy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuestSever implements ServerPolicy {
    private final int port;
    private final GuestHandler clientHandler;
    private volatile boolean run;
    private final HashMap<Integer,ClientHandler> guests;
    public volatile AtomicInteger turnGuests = new AtomicInteger();

    public GuestSever(int port, GuestHandler clientHandler) {
        turnGuests.set(0);
        this.port = port;
        this.clientHandler = clientHandler;
        clientHandler.setServer(this);
        this.run = true;
        guests = new HashMap<>();
    }
    public void nextTurn(){
        turnGuests.addAndGet(1);
        if (turnGuests.get() > guests.size()){
            turnGuests.set(1);
        }
        turnGuests.notifyAll();
    }
    public int addGuest(GuestHandler guest){
        guests.put(guests.values().size() + 2,guest);
        return guests.values().size() + 1;
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
