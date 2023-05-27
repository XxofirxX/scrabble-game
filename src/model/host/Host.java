package model.host;

import model.Player;
import server.BookScrabbleHandler;
import server.MyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Host implements Player {
    private final MyServer server;
    private final Board board;
    HashMap<String, Function<String , String>> functions = new HashMap<>();
    public Host(Integer portServer) {
        this.server = new MyServer(portServer,new BookScrabbleHandler());
        this.board = Board.getBoard();
        setUp(portServer);
    }

    public void setUp(Integer port){
        functions.put("C", text -> sendToServer(port,"C" + text));
        functions.put("P", text -> {
            String[] values = text.split(",");
            Word word = new Word(stringToTiles(values[0]),Integer.parseInt(values[1]),Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]));
            if (!Board.getBoard().boardLegal(word)){
                return "not board legal";
            } else if (!Boolean.parseBoolean(sendToServer(port,"Q"+values[0]))) {
                return "not board query";
            }
            else{
                return "legal";
            }
        });
    }
    private String sendToServer(Integer port, String word){
        Socket theServer = null;
        try {
            theServer = new Socket("localhost", port);
            BufferedReader serverInput=new BufferedReader(new InputStreamReader(theServer.getInputStream()));
            PrintWriter outToServer=new PrintWriter(theServer.getOutputStream());
            outToServer.println(word);
            return serverInput.readLine();
        } catch (IOException ignored) {
        }
        finally {
            if (theServer != null) {
                try {
                    theServer.close();
                } catch (IOException ignored) {}
            }
        }
        return String.valueOf(false);
    }
    private Tile[] stringToTiles(String tiles){
        List<Tile> wordTiles = new ArrayList<>();
        for (char c : tiles.toCharArray()){
            wordTiles.add(Tile.Bag.getBag().getTile(c));
        }
        return (Tile[]) wordTiles.toArray();
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
    public void placement(Word word) {
        if(functions.get("P").apply(convertString(word)).equals("legal"))
            board.tryPlaceWord(word);

    }

    @Override
    public void challenge(Word word) {
        functions.get("C").apply(convertString(word));
    }
}
