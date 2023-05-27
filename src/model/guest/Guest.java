package model.guest;
import model.Player;
import model.host.Tile;
import model.host.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Guest implements Player {
    private Socket theServer;
    private BufferedReader serverInput;
    private PrintWriter outToServer;
    public Guest(String ip,int port) {
        try {
            Socket theServer=new Socket(ip, port);

            BufferedReader serverInput=new BufferedReader(new InputStreamReader(theServer.getInputStream()));
            PrintWriter outToServer=new PrintWriter(theServer.getOutputStream());

        } catch (IOException e) {}
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
        String finalWord=convertString(word);
        outToServer.println("P"+finalWord+","+word.getRow()+","+word.getCol()+","+word.isVertical());
    }

    @Override
    public void challenge(Word word) {
        String finalWord=convertString(word);
        outToServer.println("C"+finalWord);
    }

    public void close() {
        try {
            theServer.close();
            serverInput.close();
            outToServer.close();
        } catch (IOException e) {}
    }
}
