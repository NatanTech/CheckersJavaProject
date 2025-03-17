package ver6;

import java.io.Serializable;
import java.util.ArrayList;
import static ver6.Piece.PieceColor.WHITE_PLAYER;

/**
 * Board 
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Board implements Serializable
{
    private Piece[][] logicBoard; // game board
    private static int ROWS = 8; // rows of board
    private static int COLS = 8; // cols of board

    //create empty board
    public Board()
    {
        logicBoard = new Piece[8][8];
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                logicBoard[i][j] = null;
            }
        }

    }

    //Copy constructor
    public Board(Board board)
    {
        logicBoard = new Piece[8][8];
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Location loc = new Location(i, j);
                Piece p = board.getPiece(loc);
                if(p != null)
                {
                    p = new Piece(p);
                }
                logicBoard[i][j] = p;
            }
        }
    }

    public Piece[][] getLogicBoard()
    {
        return logicBoard;
    }

    public Piece getPiece(Location loc)
    {
        return getPiece(loc.getRow(), loc.getCol());
    }

    public Piece getPiece(int row, int col)
    {
        return logicBoard[row][col];
    }

    //set piece on this board on loc
    public void setPiece(Location loc, Piece piece)
    {
        if(piece != null)
        {
            piece.setPieceLoc(new Location(loc));
        }
        logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
    }

   //set piece on copied board on loc
    public void setPieceOnCopiedBoard(Location loc, Piece piece)
    {
        logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
    }

    //sets piece on this board by the pieceLoc
    public void setPiece(Piece piece)
    {
        if(piece != null)
        {
            Location loc = piece.getPieceLoc();
            logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
            //System.out.println("set piece! piece " + piece);
        }
    }

    // Places more than one piece on board
    public void setListPieces(ArrayList<Piece> list)
    {
        for(Piece piece : list)
        {
            setPiece(piece);
        }
    }

    //delete piece from loc
    public void clearSquar(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()] = null;
    }

   //Checks if squar is empty
    public boolean isEmptySquar(Location loc)
    {
        return (this.getPiece(loc) == null);
    }

    //Makes king the piece on loc
    public void makeKing(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()].setKing(true);
    }

    //Makes exist board empty
    public void clearBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                clearSquar(new Location(i, j));
            }
        }
    }

    //receive String and makes the board by him
    public void strToBoard(String board)
    {
        int index = 0;
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                char c = board.charAt(index);
                if(c == '-')
                {
                    this.setPiece(null);
                }

                else
                {
                    Piece piece = new Piece(WHITE_PLAYER, new Location(i, j), false);
                    piece.charToPiece(c);
                    this.setPiece(piece);
                }

                index++;
            }
        }
    }

    //receive board and return String that represents the board
    public String boardToStr()
    {
        String str = "";
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = this.getPiece(new Location(i, j));
                if(piece == null)
                {
                    str += "-";
                }

                else
                {
                    str += piece.getCharOfPiece();
                }
            }
        }
        return str;
    }

    //print the board as model (String of 8X8)
    public void printBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = this.getPiece(new Location(i, j));
                //System.out.println(piece.getCharOfPiece());
                if(piece == null)
                {
                    System.out.print("-");
                }
                else
                {
                    //System.out.println("not null");
                    char c = piece.getCharOfPiece();
                    System.out.print(c);
                }
            }
            System.out.println("");
        }
    }
}
