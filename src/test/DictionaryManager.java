package test;

import java.util.*;
import java.util.function.BiFunction;

public class DictionaryManager {
    private final HashMap<String, Dictionary> dictionars;
    private static DictionaryManager dictionaryManager;

    private DictionaryManager() {
        this.dictionars = new HashMap<>();
    }
    private void addIfNeedBook(String book){
        if(!dictionars.containsKey(book))
            dictionars.put(book,new Dictionary(book));
    }
    private boolean contians(List<String> books, String word, BiFunction<Dictionary,String,Boolean> function){
        boolean contains = false;
        for (String book: books){
            addIfNeedBook(book);
            contains |= function.apply(dictionars.get(book),word);
        }
        return contains;
    }
    public boolean query(String... booksAndWord){
        return contians(Arrays.asList(Arrays.copyOfRange(booksAndWord, 0, booksAndWord.length - 1)), booksAndWord[booksAndWord.length-1], Dictionary::query);
    }
    public boolean challenge(String... booksAndWord){
        return contians(Arrays.asList(Arrays.copyOfRange(booksAndWord, 0, booksAndWord.length - 1)), booksAndWord[booksAndWord.length-1], Dictionary::challenge);
    }

    public int getSize(){
        return dictionars.size();
    }

    public static DictionaryManager get() {
        if (dictionaryManager == null)
            dictionaryManager = new DictionaryManager();
        return dictionaryManager;
    }
}
