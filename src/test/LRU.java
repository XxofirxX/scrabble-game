package test;

import java.util.ArrayList;
import java.util.List;

public class LRU implements CacheReplacementPolicy{
    private final List<String> words;
    public LRU(){
        words = new ArrayList<>();
    }
    @Override
    public void add(String word) {
        words.remove(word);
        words.add(word);
    }

    @Override
    public String remove() {
        return words.remove(0);
    }
}
