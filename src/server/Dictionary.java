package server;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Dictionary {
    private final String[] filesName;
    private final CacheManager wordsExists;
    private final CacheManager wordsNotExists;
    private final BloomFilter bloomFilter;

    public Dictionary(String... fileNames) {
        filesName = fileNames;
        this.wordsExists = new CacheManager(400,new LRU());
        this.wordsNotExists = new CacheManager(100,new LFU());
        this.bloomFilter = new BloomFilter(256,"MD5","SHA1");
        for (String s:filesName) {
            Scanner scanWord;
            try {
                scanWord = new Scanner(new BufferedReader(new FileReader(s)));
                while (scanWord.hasNext()) {
                    bloomFilter.add(scanWord.next());
                }
                scanWord.close();
            } catch (FileNotFoundException ignored) {}
        }
    }

    public boolean query(String word){
        if(wordsExists.query(word)){return true;}
        if(wordsNotExists.query(word)){return false;}
        boolean valueOfBloomFilter = bloomFilter.contains(word);
        if (valueOfBloomFilter){wordsExists.add(word);}
        else{wordsNotExists.add(word);}
        return valueOfBloomFilter;
    }

    public boolean challenge(String word){
        try {
            boolean valueOfIOSearcher = IOSearcher.search(word,filesName);
            if (valueOfIOSearcher){wordsExists.add(word);}
            else{wordsNotExists.add(word);}
            return valueOfIOSearcher;
        } catch (FileNotFoundException e) {
            wordsNotExists.add(word);
            return false;
        }
    }

}
