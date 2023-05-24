package test;


import java.util.HashSet;

public class CacheManager {
	private final int maxSize;
    private final CacheReplacementPolicy crp;

    private final HashSet<String> cache;

    public CacheManager(int maxSize, CacheReplacementPolicy crp) {
        this.maxSize = maxSize;
        this.crp = crp;
        this.cache = new HashSet<>();
    }
    public boolean query(String word){
        return cache.contains(word);
    }
    public void add(String word){
        crp.add(word);
        cache.add(word);
        if(cache.size() > maxSize)
            cache.remove(crp.remove());
    }
}
