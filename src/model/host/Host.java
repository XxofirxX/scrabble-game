package model.host;

import server.BookScrabbleHandler;
import server.MyServer;

public class Host {
    private final MyServer server;
    private final MyServer guestServer;
    private final Board board;

    public Host(Integer portServer,Integer portGuest) {
        this.guestServer = new MyServer(portGuest,new GuestHandler(portServer));
        this.server = new MyServer(portServer,new BookScrabbleHandler());
        this.board = Board.getBoard();
    }

}
