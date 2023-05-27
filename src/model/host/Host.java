package model.host;

import model.Player;
import server.*;

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

    private String TilesArrToString(Tile[][] tiles){
        StringBuilder tilesArr = new StringBuilder();
        for (int i = 0; i < tiles.length ;i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if(tiles[i][j] != null){
                    tilesArr.append("row:").append(i).append(",col:").append(j).append(",val:").append(tiles[i][j]).append(";");
                    //value for each part => row:0,col:0,val:h;
                    //can be split by ';' to each part in matrix
                    //can be split by ',' to each val in the part in matrix
                }
            }
        }
        return tilesArr.toString();
    }

    @Override
    public Boolean placement(Word word) {
        if (!board.boardLegal(word)){return false;}
        else if (! Boolean.parseBoolean(SD.query(convertString(word), Books.getBooksClass().getBooks()))) {return false;}
        return true;
    }

    @Override
    public Boolean challenge(Word word) {
        if (! Boolean.parseBoolean(SD.challenge(convertString(word),Books.getBooksClass().getBooks()))) {return false;}
        return true;
    }

    @Override
    public String updateBoard() {
        return TilesArrToString(board.getTiles());
    }

    @Override
    public void disconnect() {
        serverDictionary.close();
        guestServer.close();
    }
}
