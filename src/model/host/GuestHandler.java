package model.host;

import server.ClientHandler;
import server.ServerDictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

public class GuestHandler implements ClientHandler {
    HashMap<Character, BiFunction<String, String[], String>> functions = new HashMap<>();
    private final ServerDictionary serverDictionary;
    PrintWriter out;

    public GuestHandler(int port) {
        this.serverDictionary = new ServerDictionary(port);
        functions.put('C', this::challengeServer);
        functions.put('P', (word, books) -> {
            String[] values = word.split(",");
            Word word1 = new Word(stringToTiles(values[0]),Integer.parseInt(values[1]),Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]));
            if (!checkBoardValidation(word1)){return "not board legal";}
            else if (!checkDictionaryValidation(word,books)) {return "not dictionary legal";}
            return "legal";
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
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        Scanner scan = new Scanner(new BufferedInputStream(inFromclient));
        out = new PrintWriter(outToClient);
        String text = scan.next();
        Character action = text.charAt(0);
        text = text.substring(1);
        String[] books = new String[2]; //TODO fix where books come from
        out.println(functions.get(action).apply(text,books));
        scan.close();
    }

    @Override
    public void close() {
        out.close();
    }
}
