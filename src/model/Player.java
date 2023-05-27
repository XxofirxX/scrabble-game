package model;

import model.host.Tile;
import model.host.Word;

public interface Player {
    Boolean placement(Word word);
    Boolean challenge(Word word);
    String updateBoard();
    void disconnect();
}
