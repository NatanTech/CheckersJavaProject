package ver7;

import java.io.Serializable;
import java.util.ArrayList;
import static ver7.Piece.PieceColor.*;

/**
 * Document : Board Created on : 17/08/2021, 23:46:46 Author : beita
 */
public class Board implements Serializable
{
    private Piece[][] logicBoard; // game board
    private static int ROWS = 8; // rows of board
    private static int COLS = 8; // cols of board

    /**
     * Constructor to create empty board
     */
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

    /**
     * Copy constructor
     *
     * @param board
     */
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

    /**
     * sets piece on this board on loc
     *
     * @param loc on him place the piece
     * @param piece that placed on loc
     */
    public void setPiece(Location loc, Piece piece)
    {
        if(piece != null)
        {
            piece.setPieceLoc(new Location(loc));
        }
        logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
    }

    /**
     * sets piece on copied board on loc
     *
     * @param loc
     * @param piece
     */
    public void setPieceOnCopiedBoard(Location loc, Piece piece)
    {
        logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
    }

    /**
     * sets piece on this board by the pieceLoc
     *
     * @param piece
     */
    public void setPiece(Piece piece)
    {
        if(piece != null)
        {
            Location loc = piece.getPieceLoc();
            logicBoard[loc.getRow()][loc.getCol()] = new Piece(piece);
            //System.out.println("set piece! piece " + piece);
        }
    }

    /**
     * Places more than one piece on board
     *
     * @param list
     */
    public void setManyPieces(ArrayList<Piece> list)
    {
        for(Piece piece : list)
        {
            setPiece(piece);
        }
    }

    /**
     * delete piece from loc
     *
     * @param loc
     */
    public void makeEmptyTile(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()] = null;
    }

    /**
     * Checks if the tile of loc is empty tile
     *
     * @param loc
     * @return true if tile is empty, false else
     */
    public boolean isEmptyTile(Location loc)
    {
        return (this.getPiece(loc) == null);
    }

    /**
     * Makes king the piece on loc
     *
     * @param loc on him the piece beccome king
     */
    public void makeKing(Location loc)
    {
        logicBoard[loc.getRow()][loc.getCol()].setKing(true);
    }

    /**
     * Makes exist board empty
     */
    public void clearBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                makeEmptyTile(new Location(i, j));
            }
        }
    }

    /**
     * receive String and makes the board by him
     *
     * @param board
     */
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
                    piece.signToPiece(c);
                    this.setPiece(piece);
                }

                index++;
            }
        }
    }

    /**
     * receive board and return String that represents the board
     *
     * @return
     */
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
                    str += piece.getSignOfPiece();
                }
            }
        }
        return str;
    }

    /**
     * print the board as model (String of 8X8)
     */
    public void printBoard()
    {
        for(int i = 0; i < ROWS; i++)
        {
            for(int j = 0; j < COLS; j++)
            {
                Piece piece = this.getPiece(new Location(i, j));
                //System.out.println(piece.getSignOfPiece());
                if(piece == null)
                {
                    System.out.print("-");
                }
                else
                {
                    //System.out.println("not null");
                    char c = piece.getSignOfPiece();
                    System.out.print(c);
                }
            }
            System.out.println("");
        }
    }
}
