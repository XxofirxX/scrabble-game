package test;

import java.util.ArrayList;

public class Board {
    private static Board board;
    private static final Tile[][] bArray =new Tile[15][15];

    private static final String[][] sArray=new String[15][15];

    private Board()
    {
        //this is red
        sArray[0][0] = "triple word";
        sArray[0][7]= "triple word";
        sArray[0][14]="triple word";
        sArray[7][0]="triple word";
        sArray[7][14]="triple word";
        sArray[14][0]="triple word";
        sArray[14][7]="triple word";
        sArray[14][14]="triple word";
        //this is cyan
        sArray[0][3] ="double letter";
        sArray[0][11]= "double letter";
        sArray[2][6]="double letter";
        sArray[2][8]="double letter";
        sArray[3][0]="double letter";
        sArray[3][7]="double letter";
        sArray[3][14]="double letter";
        sArray[6][2]="double letter";
        sArray[6][6]="double letter";
        sArray[6][8] = "double letter";
        sArray[6][12]= "double letter";
        sArray[7][3]="double letter";
        sArray[7][11]="double letter";
        sArray[8][2]="double letter";
        sArray[8][6]="double letter";
        sArray[8][8]="double letter";
        sArray[8][12]="double letter";
        sArray[11][0]="double letter";
        sArray[11][7] ="double letter";
        sArray[11][14]="double letter";
        sArray[12][6]="double letter";
        sArray[12][8]="double letter";
        sArray[14][3]="double letter";
        sArray[14][11]="double letter";
        //this is blue
        sArray[1][5] = "triple letter";
        sArray[1][9]= "triple letter";
        sArray[5][1]="triple letter";
        sArray[5][5]="triple letter";
        sArray[5][9]="triple letter";
        sArray[5][13]="triple letter";
        sArray[9][1]="triple letter";
        sArray[9][5]="triple letter";
        sArray[9][9]="triple letter";
        sArray[9][13] = "triple letter";
        sArray[13][5]= "triple letter";
        sArray[13][9]="triple letter";
        //this is yellow
        sArray[1][1] = "double word";
        sArray[1][13]= "double word";
        sArray[2][2]="double word";
        sArray[2][12]="double word";
        sArray[3][3]="double word";
        sArray[3][11]="double word";
        sArray[4][4]="double word";
        sArray[4][10]="double word";
        sArray[10][4] ="double word";
        sArray[10][10]="double word";
        sArray[11][3]="double word";
        sArray[11][11] ="double word";
        sArray[12][2]= "double word";
        sArray[12][12]="double word";
        sArray[13][1]="double word";
        sArray[13][13]="double word";
        //this is a star
        sArray[7][7]="star";
    }
    public static Board getBoard() {
        if(board==null)
            board=new Board();
        return board;
    }
    public Tile[][] getTiles()
    {
        return bArray.clone();
    }
    private Tile tileFromLoc(int row, int col, Word word){
        if(bArray[row][col] != null){return bArray[row][col];}
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.isVertical()){
                if (word.getRow() + i == row && word.getCol()==col){
                    return word.getTiles()[i];
                }
            }
            else {
                if (word.getRow() == row && word.getCol() + i == col){
                    return  word.getTiles()[i];
                }
            }
        }
        return null;
    }
    public boolean boardLegal(Word word)
    {
        return inBoard(word) && isLaying(word);
    }
    private boolean inBoard(Word word)
    {
        int length=word.getTiles().length;
        int row=word.getRow();
        int col=word.getCol();
        if((row<0||row>14)||(col<0||col>15)) {
            return false;
        }
        if (word.isVertical()) {
            return (15 - row) >= length;
        }
        return (15 - col) >= length;
    }
    private boolean isLaying(Word word)
    {
        int row=word.getRow();
        int col=word.getCol();
        int i;
        boolean flag=false;
        if(word.isVertical())
            i=row;
        else
            i=col;
        int length=word.getTiles().length+i;
        for (; i<length; i++)
        {
            if(word.isVertical())
            {
                if (bArray[i][col] != null)
                {
                    if(word.getTiles()[i - row]!=null)
                    {
                        if (bArray[i][col] != word.getTiles()[i - row]) {return false;}
                        else{flag=true;}
                    }
                    else
                    {
                        flag=true;
                    }
                }
                else
                {
                    if (word.getTiles()[i - row]==null){return false;}
                    if((col+1)<15)
                    {

                        if(bArray[i][col+1]!=null){flag=true;}
                    }
                    if((col-1)>=0)
                    {
                        if(bArray[i][col-1]!=null){flag=true;}
                    }
                }
                if((word.getCol()==7)&&(i==7))
                    flag=true;
            }
            else
            {
                if (bArray[row][i] != null)
                {
                    if(word.getTiles()[i - col]!=null)
                    {
                        if (bArray[row][i] != word.getTiles()[i - col]) {return false;}
                        else{flag=true;}
                    }
                    else
                    {
                        flag=true;
                    }
                }
                else
                {
                    if (word.getTiles()[i - col]==null){return false;}
                    if((row+1)<15)
                    {
                        if(bArray[row+1][i]!=null){flag=true;}
                    }
                    if((row-1)>=0)
                    {
                        if(bArray[row-1][i]!=null){flag=true;}
                    }
                }
                if((word.getRow()==7)&&(i==7))
                    flag=true;
            }

        }
        return flag;
    }

    private boolean dictionaryLegal(Word word)
    {
        return true;
    }

    private int getScore(Word word)
    {
        int row=word.getRow();
        int col=word.getCol();
        int i;
        int score=0;
        int num=1;
        if(word.isVertical()) {i = row;}
        else{ i = col;}
        int length=word.getTiles().length + i;
        for(;i<length;i++)
        {
           if(word.isVertical())
           {
               num*=multipleWord(i,col);
               score += multipleLetter(i,col,word);
           }
           else
           {
               num*=multipleWord(row,i);
               score += multipleLetter(row,i,word);
           }
       }
        score*=num;
        return score;
    }
    private int multipleLetter(int row,int col,Word word)
    {
        int lScore = tileFromLoc(row,col,word).score;
        if (sArray[row][col] == null){return lScore;}
        if (sArray[row][col].equals("triple letter"))
        {
            lScore *= 3;
        }
        else if (sArray[row][col].equals("double letter"))
        {
            lScore *= 2;
        }
        return lScore;
    }
    private int multipleWord(int row,int col)
    {
        int num=1;
        if (sArray[row][col] == null){return num;}
        if(sArray[row][col].equals("triple word"))
            num*=3;
        if(sArray[row][col].equals("double word"))
            num*=2;
        if(sArray[row][col].equals("star")&&(bArray[7][7]==null))
        {
            num*=2;
        }
        return num;
    }
    private ArrayList<Word> getWords(Word word)
    {
        ArrayList<Word> words=new ArrayList<>();
        int row=word.getRow();
        int col=word.getCol();
        int i;
        int rowWord;
        int colWord;
        if(word.isVertical()){ i=row;}
        else{ i=col;}
        int length=word.getTiles().length+i;
        Word w;
        for (;i<length;i++)
        {
            if(word.isVertical())
            {
                if(word.getTiles()[i-row]!=null) {
                    colWord = findFirstLetter(i, col, false);
                    w = newWord(i, colWord, false, word);
                    if (w.getTiles().length > 1) {words.add(w);}
                }
            }
            else
            {
                if(word.getTiles()[i-col]!=null) {
                    rowWord = findFirstLetter(row, i, true);
                    w = newWord(rowWord, i, true, word);
                    if (w.getTiles().length > 1) {words.add(w);}
                }
            }

        }
        if(word.isVertical())
        {
            rowWord=findFirstLetter(row,col,true);
            words.add(newWord(rowWord,col,true,word));
        }
        else
        {
            colWord=findFirstLetter(row,col,false);
            words.add(newWord(row,colWord,false,word));
        }
        return words;
    }
    private int findFirstLetter(int row,int col,boolean vertical) //finds the first letter of a word
    {
        if(vertical)
        {
            while(bArray[row-1][col]!=null)
            {
                row--;
            }
            return row;
        }
        while(bArray[row][col-1]!=null)
        {
            col--;
        }
        return col;
    }

    private Word newWord(int row,int col,boolean vertical,Word word)
    {
        ArrayList<Tile> tiles = new ArrayList<>();
        int i = row;
        int j = col;
        while(tileFromLoc(i,j,word) != null)
        {
            tiles.add(tileFromLoc(i,j,word));
            if(vertical){i++;}
            else{j++;}

        }
        Tile[] newTile=new Tile[tiles.size()];
        for(int k=0;k<tiles.size();k++)
        {
            newTile[k]=tiles.get(k);
        }
        return new Word(newTile,row,col,vertical);
    }
    public int tryPlaceWord(Word word)
    {
        int score=0;
        if(!(boardLegal(word)&&dictionaryLegal(word))){return 0;}
        ArrayList<Word> arrayWords=getWords(word);
        for (Word arrayWord : arrayWords)
        {
            if (!(boardLegal(arrayWord) && dictionaryLegal(arrayWord)))
                return 0;
            score += getScore(arrayWord);
        }
        int row=word.getRow();
        int col=word.getCol();
        int i;
        if(word.isVertical()){i=row;}
        else{i=col;}
        int length=word.getTiles().length +i;
        for (; i < length; i++)
        {
            if(word.isVertical())
            {
                if(word.getTiles()[i - row] != null)
                    bArray[i][col]=word.getTiles()[i-row];
            }
            else
            {
                if(word.getTiles()[i - col] != null)
                    bArray[row][i]=word.getTiles()[i-col];
            }
        }
        return score;
    }
}