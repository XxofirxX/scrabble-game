package server;

public interface ServerDictionaryPolicy {
     String query(String word, String[] books);
     String challenge(String word, String[] books);
}
