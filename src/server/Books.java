package server;


import java.util.ArrayList;
import java.util.List;

public class Books {
    private static Books booksClass;
    private final List<String> books;
    private Books(){
        books = new ArrayList<>();
        books.add("alice_in_wonderland.txt");
        books.add("Harray Potter.txt");
        books.add("The Matrix.txt");
    }
    public String[] getBooks(){
        return (String[]) books.toArray();
    }
    public static Books getBooksClass(){
        if(booksClass == null){booksClass = new Books();}
        return booksClass;
    }
}
