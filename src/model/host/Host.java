package model.host;

import server.BookScrabbleHandler;
import server.MyServer;
import server.ServerPolicy;

public class Host {
    private final ServerPolicy serverDictionary;
    private final ServerPolicy guestServer;
    private final Board board;

    public Host(Integer portServer,Integer portGuest) {
        this.guestServer = new GuestSever(portGuest,new GuestHandler(portServer));
        this.serverDictionary = new MyServer(portServer,new BookScrabbleHandler());
        this.board = Board.getBoard();
        guestServer.start();
        serverDictionary.start();
    }

}
