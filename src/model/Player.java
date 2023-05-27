package model;

import model.host.Tile;
import model.host.Word;

public interface Player {
    void placement(Word word);
    void challenge(Word word);
}
