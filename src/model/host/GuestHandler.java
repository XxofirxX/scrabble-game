package model.host;

import server.ClientHandler;
import server.ServerDictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class GuestHandler extends Thread implements ClientHandler {
    HashMap<Character, BiFunction<String, String[], String>> functions = new HashMap<>();
    private final ServerDictionary serverDictionary;
    private InputStream inFromClient;
    private OutputStream outToClient;
    private GuestSever server;

    public GuestHandler(int port) {
        this.serverDictionary = new ServerDictionary(port);
        functions.put('C', this::challengeServer);
        functions.put('P', (word, books) -> {
            String[] values = word.split(",");
            Word word1 = new Word(stringToTiles(values[0]),Integer.parseInt(values[1]),Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]));
            if (!checkBoardValidation(word1)){return "not board legal";}
            else if (!checkDictionaryValidation(word,books)) {return "not dictionary legal";}
            return "true";
        });
    }

    private Tile[] stringToTiles(String tiles){
        List<Tile> wordTiles = new ArrayList<>();
        for (char c : tiles.toCharArray()){
            wordTiles.add(Tile.Bag.getBag().getTile(c));
        }
        return (Tile[]) wordTiles.toArray();
    }
    private boolean checkBoardValidation(Word word){
        return Board.getBoard().boardLegal(word);
    }
    private boolean checkDictionaryValidation(String word, String[] books){
        return Boolean.parseBoolean(serverDictionary.query(word,books));
    }
    private String challengeServer(String word, String[] books){
        return serverDictionary.challenge(word,books);
    }
    public void setServer(GuestSever server){
        this.server = server;
    }
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient){
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        start();
    }

    @Override
    public void close() {
        try {inFromClient.close();outToClient.close();} catch (IOException ignored) {}
    }
    private void getRoundOfGuest(Guest_Host guestHost){
        String returnFromClient = "";
        String valueInFromClient;
        while (!returnFromClient.equals("true")){
            guestHost.sendToGuest("your turn");
            valueInFromClient = guestHost.getFromGuest();
            if(valueInFromClient.equals("disconnect")){break;}
            Character action = valueInFromClient.charAt(0);
            valueInFromClient = valueInFromClient.substring(1);
            String[] books = new String[2]; //TODO fix where books come from
            returnFromClient = functions.get(action).apply(valueInFromClient, books);
            guestHost.sendToGuest(returnFromClient);
        }
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
    public void run(){
        int name = server.addGuest(this);
        String valueInFromClient ;
        Guest_Host guestHost = new Guest_Host(inFromClient, outToClient);
        while (true) {
            valueInFromClient = guestHost.getFromGuest();
            if(valueInFromClient.equals("disconnect")){break;}
            if(valueInFromClient.equals("update board")){guestHost.sendToGuest(TilesArrToString(Board.getBoard().getTiles())); continue;}
            if(server.turnGuests.get() == name){
                try {
                    new Thread(() -> getRoundOfGuest(guestHost)).join();
                } catch (InterruptedException ignored) {}
                server.nextTurn();
            }
        }
        guestHost.close();
    }

}
