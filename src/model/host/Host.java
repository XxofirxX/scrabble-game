package model.host;

import model.Player;
import server.BookScrabbleHandler;
import server.MyServer;
import server.ServerPolicy;

public class Host implements Player {
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

    @Override
    public void placement(Word word) {

    }

    @Override
    public void challenge(Word word) {

    }
}
