package test;

import java.util.Arrays;
import java.util.Objects;

public class Word
{
    private boolean vertical;
    private Tile tiles[];

    private int row, col;
    public Word(Tile[] tiles, int newRow, int newCol, boolean newVertical)
    {
        this.row = newRow;
        this.col = newCol;
        this.vertical = newVertical;
        this.tiles = new Tile[tiles.length];
        System.arraycopy(tiles,0,this.tiles,0,tiles.length);
    }

    // finish col and row
    public boolean isVertical() {
        return vertical;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return vertical == word.vertical && row == word.row && col == word.col && Arrays.equals(tiles, word.tiles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(vertical, row, col);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }
}