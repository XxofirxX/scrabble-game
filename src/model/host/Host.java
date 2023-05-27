package model.host;

import model.Player;
import server.BookScrabbleHandler;
import server.MyServer;
import server.ServerDictionary;
import server.ServerPolicy;

public class Host implements Player {
    private final ServerPolicy serverDictionary;
    private final ServerPolicy guestServer;
    private final Board board;
    private final ServerDictionary SD;

    public Host(Integer portServer,Integer portGuest) {
        this.guestServer = new GuestSever(portGuest,new GuestHandler(portServer));
        this.serverDictionary = new MyServer(portServer,new BookScrabbleHandler());
        this.board = Board.getBoard();
        guestServer.start();
        serverDictionary.start();
        SD=new ServerDictionary(portServer);
    }

    public String convertString(Word word){
        StringBuilder stringBuilder=new StringBuilder();
        for(Tile t:word.getTiles()){
            stringBuilder.append(t);
        }
        String finalWord=stringBuilder.toString();
        return finalWord;
    }

    @Override
    public Boolean placement(Word word) {
        if (!board.boardLegal(word)){return false;}
        else if (! Boolean.parseBoolean(SD.query(convertString(word),new String[1]))) {return false;}
        return true;
    }

    @Override
    public Boolean challenge(Word word) {
        if (! Boolean.parseBoolean(SD.challenge(convertString(word),new String[1]))) {return false;}
        return true;
    }
}
