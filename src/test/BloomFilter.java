package test;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BloomFilter {
    private final BitSet bitsArr;
    private final LinkedList<MessageDigest> hashFunction = new LinkedList<>();


    public BloomFilter(int lengthOfBitArr, String... algorithms) {
        bitsArr = new BitSet(lengthOfBitArr);
        for (String s: algorithms) {
            try {
                hashFunction.add(MessageDigest.getInstance(s));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Set<Integer> getIndexOfWord(String word){
        Set<Integer> indexes = new HashSet<>();
        for (MessageDigest md: hashFunction) {
            byte[] bts = md.digest(word.getBytes());
            BigInteger i = new BigInteger(bts);
            int index = Math.abs(i.intValue()) % bitsArr.size();
            indexes.add(index);
        }
        return indexes;
    }
    public void add(String word){
        for (int i: getIndexOfWord(word)) {
            bitsArr.set(i,true);
        }
    }

    public boolean contains(String word){
        for (int i: getIndexOfWord(word)) {
            if (!bitsArr.get(i)){return false;}
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < bitsArr.length(); i++) {
            if (bitsArr.get(i)){string.append(1);}
            else{string.append(0);}
        }
        return string.toString();
    }
}
