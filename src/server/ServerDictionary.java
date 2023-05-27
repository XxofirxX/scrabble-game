package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Function;

public class ServerDictionary implements ServerDictionaryPolicy{
    HashMap<Character, Function<String , String>> functions = new HashMap<>();

    public ServerDictionary(Integer port) {
        functions.put('C', text -> sendToServer(port, "C," + text));
        functions.put('Q', text -> sendToServer(port, "Q," + text));
    }

    private String sendToServer(Integer port, String word){
        try (Socket theServer = new Socket("localhost", port)) {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(theServer.getInputStream()));
            PrintWriter outToServer = new PrintWriter(theServer.getOutputStream());
            outToServer.println(word);
            String returnValue = serverInput.readLine();
            theServer.close();
            return returnValue;
        } catch (IOException ignored) {}
        return String.valueOf(false);
    }
    private String combineBooksWordToString(String word, String[] books){
        StringBuilder string = new StringBuilder();
        for (String book: books) {
            string.append(book).append(',');
        }
        string.append(word);
        return string.toString();
    }
    @Override
    public String query(String word, String[] books) {
        return functions.get('Q').apply(combineBooksWordToString(word,books));
    }

    @Override
    public String challenge(String word, String[] books) {
        return functions.get('C').apply(combineBooksWordToString(word,books));
    }
}
