package model.host;

import server.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class GuestHandler implements ClientHandler {
    HashMap<String, Function<String , String>> functions = new HashMap<>();
    PrintWriter out;

    public GuestHandler(String ip, Integer port){
        functions.put("C", text -> sendToServer(ip,port,"C" + text));
        functions.put("P", text -> {
            String[] values = text.split(",");
            Word word = new Word(stringToTiles(values[0]),Integer.parseInt(values[1]),Integer.parseInt(values[2]), Boolean.parseBoolean(values[3]));
            if (!Board.getBoard().boardLegal(word)){
                return "not board legal";
            } else if (!Boolean.parseBoolean(sendToServer(ip,port,"Q"+values[0]))) {
                return "not board query";
            }
            else{
                return "legal";
            }
        });
    }
    private String sendToServer(String ip, Integer port, String word){
        Socket theServer = null;
        try {
            theServer = new Socket(ip, port);
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
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        Scanner scan = new Scanner(new BufferedInputStream(inFromclient));
        out = new PrintWriter(outToClient);
        String text = scan.next();
        String action = String.valueOf(text.charAt(0));
        text = text.substring(1);
        out.println(functions.get(action).apply(text));
        scan.close();
    }

    @Override
    public void close() {
        out.close();
    }
}
