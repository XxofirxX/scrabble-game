package model.host;

import server.BookScrabbleHandler;
import server.MyServer;
import server.ServerPolicy;

public class Host {
    private final ServerPolicy ServerDictionary;
    private final ServerPolicy guestServer;
    private final Board board;

    public Host(Integer portServer,Integer portGuest) {
        this.guestServer = new GuestSever(portGuest,new GuestHandler(portServer));
        this.ServerDictionary = new MyServer(portServer,new BookScrabbleHandler());
        this.board = Board.getBoard();
    }

}
