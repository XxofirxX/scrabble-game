package host.model;

import java.util.Objects;
import java.util.Random;
public class Tile {
    public final char letter;
    public final int score;

    public Tile(final char letter, final int score)
    {
        this.letter = letter;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag{
        private static Bag bag;
        private final int[] totalOfTiles = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        private final int[] amountOfTiles = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        private final Tile[] tiles = new Tile[26];

        private Bag(){
            for (int i = 0; i < 26; i++){
                int[] scoreOfTiles = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
                tiles[i] = new Tile((char)(i + 'A'), scoreOfTiles[i]);
            }
        }
        public static Bag getBag(){
            if (bag == null){
                bag = new Bag();
            }
            return bag;
        }
        private boolean checkEmpty(){
            for (int i= 0; i < 26; i++){
                if (amountOfTiles[i] != 0){
                    return false;
                }
            }
            return true;
        }

        public Tile getRand(){
            if(checkEmpty()){return null;}
            int randTile;
            Random rand = new Random();
            do
            {
                randTile = rand.nextInt(26);
            }while (amountOfTiles[randTile] == 0);
            amountOfTiles[randTile]--;
            return
                    tiles[randTile];
        }
        public Tile getTile(char letter){
            if ((int)letter >= 'A' && (int)letter <= 'Z'){
                int letterInt = letter - 'A'; // index
                if(amountOfTiles[letterInt] != 0)
                {
                    amountOfTiles[letterInt]--;
                    return tiles[letterInt];
                }
            }
            return null;
        }

        public void put(Tile tile){
            int letterInt =  tile.letter - 'A';
            amountOfTiles[letterInt] = Math.min(amountOfTiles[letterInt] + 1, totalOfTiles[letterInt]);
        }
        public int size(){
            int counter = 0;
            for(int i = 0; i < 26; i++){
                counter += amountOfTiles[i];
            }
            return counter;
        }
        public int[] getQuantities(){
            return amountOfTiles.clone();
        }
    }
}