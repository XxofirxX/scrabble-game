package test;


import java.util.LinkedHashMap;
import java.util.Map;

public class LFU implements CacheReplacementPolicy{

    private final LinkedHashMap<String,Integer> words;


    public LFU() {
        this.words = new LinkedHashMap<>();
    }

    private Map.Entry<String, Integer> findMin(){
        return words.entrySet().stream().min(Map.Entry.comparingByValue()).orElseThrow(() -> new RuntimeException("The LFU is empty"));
    }
    public void add(String word) {
        if (words.containsKey(word)){
            words.replace(word,words.get(word) + 1);
        }
        else{
            words.put(word,1);
        }
    }

    public String remove() {
        Map.Entry<String,Integer> element = findMin();
        int i = element.getValue() - 1;
        String s = element.getKey();
        if (i > 0){words.replace(s,i+1,i);}
        else{words.remove(s,i+1);}
        return s;
    }
}
