package test;


import java.io.*;
import java.util.*;
import java.util.function.Function;

public class BookScrabbleHandler implements ClientHandler{
    HashMap<String, Function<String[] , Boolean>> functions = new HashMap<>();
    PrintWriter out;

    public BookScrabbleHandler() {
        functions.put("C", DictionaryManager.get()::challenge);
        functions.put("Q",DictionaryManager.get()::query);
    }

    private void sendOut(PrintWriter out,Boolean message) {
        out.print(message ? "true\n" : "false\n");
    }
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        Scanner scan = new Scanner(new BufferedInputStream(inFromclient));
        out = new PrintWriter(outToClient);
        String text = scan.next();
        String[] booksAndWord = text.split(",");
        String function = booksAndWord[0];
        sendOut(out,functions.get(function).apply(Arrays.copyOfRange(booksAndWord, 1, booksAndWord.length)));
    }

    @Override
    public void close() {
        out.close();
    }
}
