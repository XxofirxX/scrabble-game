package server;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IOSearcher {
    public static boolean search(String word,String... fileNames) throws FileNotFoundException {
        for (String s:fileNames) {
            Scanner scanWord = new Scanner(new BufferedReader(new FileReader(s)));
            while (scanWord.hasNext()){
                if (scanWord.next().equals(word)){
                    return true;
                }
            }
            scanWord.close();
        }
        return false;
    }
}
